package android.bg.ro.boardgame.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.models.BoardGame;
import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.utils.Constant;
import android.os.Handler;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;
import android.widget.ImageView;

import co.yellowbricks.bggclient.BGG;
import co.yellowbricks.bggclient.common.ThingType;
import co.yellowbricks.bggclient.fetch.FetchException;
import co.yellowbricks.bggclient.fetch.domain.FetchItem;

public class ImageLoader implements TaskBoardGame, TaskDelegate{

    // Initialize MemoryCache
    MemoryCache memoryCache = new MemoryCache();
    TaskBoardGame taskBoardGame;
    TaskDelegate taskDelegate;
    ImageView imageView;
    FileCache fileCache;

    //Create Map (collection) to store image and image url in key value pair
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(
            new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    GenericHttpService genericHttpService;

    //handler to display images in UI thread
    Handler handler = new Handler();

    public ImageLoader(Context context){

        fileCache = new FileCache(context);
        // Creates a thread pool that reuses a fixed number of
        // threads operating off a shared unbounded queue.
        executorService=Executors.newFixedThreadPool(5);

    }

    public ImageLoader(Context context, ImageView imageView){

        fileCache = new FileCache(context);
        this.imageView = imageView;
        // Creates a thread pool that reuses a fixed number of
        // threads operating off a shared unbounded queue.
        executorService=Executors.newFixedThreadPool(5);

    }



    // default image show in list (Before online image download)
    final int stub_id= R.drawable.logored;

    public void DisplayImage(String url, ImageView imageView)
    {
        //Store image and url in Map
        imageViews.put(imageView, url);

        //Check image is stored in MemoryCache Map or not (see MemoryCache.java)
        Bitmap bitmap = memoryCache.get(url);

        if(bitmap!=null){
            // if image is stored in MemoryCache Map then
            // Show image in listview row
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 100, 100, false));
        }
        else
        {
            //queue Photo to download from url
            queuePhoto(url, imageView);

            //Before downloading image show default image
            imageView.setImageResource(stub_id);
        }
    }

    public void Display(Context context, int id, ImageView imageView)
    {
        taskBoardGame = this;
        List<Pair<String, String>> params = new ArrayList<>();
        params.add(new Pair<>("id", String.valueOf(id)));
        BoardGameDetailsService boardGameService = (BoardGameDetailsService) new BoardGameDetailsService(context, params,taskBoardGame).execute();
        //Store image and url in Map

    }

    public void DisplayUser(Context context, int id, ImageView imageView)
    {
        taskDelegate = this;
        URL url = null;
        try {
            url = new URL("http://" + Constant.IP +"/searchUser");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
        parameters.add(new Pair<>("userId",String.valueOf(id)));
        parameters.add(new Pair<>("searchUserId",String.valueOf(id)));

        genericHttpService = (GenericHttpService) new GenericHttpService(context,"searchUser", parameters,taskDelegate).execute(url);

    }



    private void queuePhoto(String url, ImageView imageView)
    {
        // Store image and url in PhotoToLoad object
        PhotoToLoad p = new PhotoToLoad(url, imageView);

        // pass PhotoToLoad object to PhotosLoader runnable class
        // and submit PhotosLoader runnable to executers to run runnable
        // Submits a PhotosLoader runnable task for execution

        executorService.submit(new PhotosLoader(p));
    }

    @Override
    public void TaskCompletionResult(String result) {
        CustomParser customParser = new CustomParser();
        User searchUser = customParser.getSearchUser(genericHttpService.getResponse());

        BitmapFactory.Options options = new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
        options.inPurgeable = true; // inPurgeable is used to free up memory while required
        Bitmap songImage = BitmapFactory.decodeByteArray(searchUser.getProfilePicture(), 0, searchUser.getProfilePicture().length, options);//Decode image, "thumbnail" is the object of image file
        imageView.setImageBitmap(songImage);
    }


    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u;
            imageView=i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }

        @Override
        public void run() {
            try{
                //Check if image already downloaded
                if(imageViewReused(photoToLoad))
                    return;
                // download image from web url
                Bitmap bmp = getBitmap(photoToLoad.url);

                // set image data in Memory Cache
                memoryCache.put(photoToLoad.url, bmp);

                if(imageViewReused(photoToLoad))
                    return;

                // Get bitmap to display
                BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);

                // Causes the Runnable bd (BitmapDisplayer) to be added to the message queue.
                // The runnable will be run on the thread to which this handler is attached.
                // BitmapDisplayer run method will call
                handler.post(bd);

            }catch(Throwable th){
                th.printStackTrace();
            }
        }
    }

    private Bitmap getBitmap(String url)
    {
        File f=fileCache.getFile(url);

        //from SD cache
        //CHECK : if trying to decode file which not exist in cache return null
        Bitmap b = decodeFile(f);
        if(b!=null)
            return b;

        // Download image file from web
        try {

            Bitmap bitmap=null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();

            // Constructs a new FileOutputStream that writes to file
            // if file not exist then it will create file
            OutputStream os = new FileOutputStream(f);

            // See Utils class CopyStream method
            // It will each pixel from input stream and
            // write pixels to output stream (file)
            Utils.CopyStream(is, os);

            os.close();
            conn.disconnect();

            //Now file created and going to resize file with defined height
            // Decodes image and scales it to reduce memory consumption
            bitmap = decodeFile(f);

            return bitmap;

        } catch (Throwable ex){
            ex.printStackTrace();
            if(ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }

    //Decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){

        try {

            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1=new FileInputStream(f);
            BitmapFactory.decodeStream(stream1,null,o);
            stream1.close();

            //Find the correct scale value. It should be the power of 2.

            // Set width/height of recreated image
            final int REQUIRED_SIZE=85;

            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2 < REQUIRED_SIZE || height_tmp/2 < REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //decode with current scale values
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            FileInputStream stream2=new FileInputStream(f);
            Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;

        } catch (FileNotFoundException e) {
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    boolean imageViewReused(PhotoToLoad photoToLoad){

        String tag=imageViews.get(photoToLoad.imageView);
        //Check url is already exist in imageViews MAP
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;

            // Show bitmap on UI
            if(bitmap!=null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        //Clear cache directory downloaded images and stored data in maps
        memoryCache.clear();
        fileCache.clear();
    }

    @Override
    public void searchGame(List<BoardGame> boardGames) {
        for(BoardGame game : boardGames) {
            imageViews.put(imageView, game.getPicture());

            //Check image is stored in MemoryCache Map or not (see MemoryCache.java)
            Bitmap bitmap = memoryCache.get(game.getPicture());

            if(bitmap!=null){
                // if image is stored in MemoryCache Map then
                // Show image in listview row
                imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 50, 50, false));
            }
            else
            {
                //queue Photo to download from url
                queuePhoto(game.getPicture(), imageView);

                //Before downloading image show default image
                imageView.setImageResource(stub_id);
            }
        }
    }

}