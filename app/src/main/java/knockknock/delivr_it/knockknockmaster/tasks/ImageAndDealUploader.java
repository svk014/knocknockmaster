package knockknock.delivr_it.knockknockmaster.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import knockknock.delivr_it.knockknockmaster.tasks.OfferUpdateTask;

public class ImageAndDealUploader {

    private Context context;
    private HashMap config;
    private ByteArrayInputStream inputStream;
    private Map uploadedImageMetaData;
    private String title;
    private String text;
    private String imagePath;


    public ImageAndDealUploader(Context context, String title, String text, String imagePath) {
        this.context = context;
        this.title = title;
        this.text = text;
        this.imagePath = imagePath;
    }

    public void uploadImage() throws IOException {
        config = new HashMap();
        config.put("cloud_name", "dscnotrpw");
        config.put("api_key", "733992825548662");
        config.put("api_secret", "cCXtx8fEjx6f4RyWWIcMo5DRF9E");

        File image = new File(imagePath);

        inputStream = new ByteArrayInputStream(FileUtils.readFileToByteArray(image));
        new ImageUploadTask().execute();
    }

    private class ImageUploadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Cloudinary cloudinary = new Cloudinary(config);
            uploadedImageMetaData = null;
            try {
                uploadedImageMetaData = cloudinary.uploader().upload(inputStream, ObjectUtils.emptyMap());
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            if (uploadedImageMetaData != null) {

                Toast.makeText(context, "DONE Uploading Image", Toast.LENGTH_LONG).show();
                Toast.makeText(context, "Uploading offer details", Toast.LENGTH_LONG).show();
                OfferUpdateTask offerUpdateTask = new OfferUpdateTask(context, title, text, uploadedImageMetaData.get("url").toString(), imagePath);
                offerUpdateTask.execute();

            } else {
                Toast.makeText(context, "Error Uploading Image", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
