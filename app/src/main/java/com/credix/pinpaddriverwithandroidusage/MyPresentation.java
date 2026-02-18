package com.credix.pinpaddriverwithandroidusage;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pixplicity.sharp.Sharp;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

public class MyPresentation extends Presentation {
    private ImageView img;
    private VideoView vid;
    private YouTubePlayerView you_tube_player;
    private YouTubePlayer player;
    private YouTubePlayer youTubePlayer;
    private TableLayout tableLayout;
    private TableRow row;
    private Context context;
    private List<Product> productList;
    private ScrollView scrollView;
    private TextView totalQuantityTextView;
    private TextView totalPriceTextView;
    private TextView alternativeTextView;
    private ConstraintLayout fixedTitle;
    private ConstraintLayout summaryRow;
    private ConstraintLayout titleRow;
    private ConstraintLayout fixedImagesContainer;
    private VideoView videoView;
    private ImageView imageView;
    private ImageView logo_first;
    private ImageView logo_secend;


    public MyPresentation(Context outerContext,
                          Display display) {
        super(outerContext, display);
    }

    public MyPresentation(Context outerContext, Display display, List<Product> productList) {
        super(outerContext, display);
        this.context = outerContext;
        this.productList = productList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        scrollView = findViewById(R.id.scrollView);
        tableLayout = findViewById(R.id.tableLayout);
        totalPriceTextView = findViewById(R.id.totalPrice);
        alternativeTextView = findViewById(R.id.alternateText);
        summaryRow = findViewById(R.id.summaryRow);
        fixedTitle = findViewById(R.id.fixedTitle);
        fixedImagesContainer = findViewById(R.id.fixedImagesContainer);

        videoView = findViewById(R.id.videoView);
        imageView = findViewById(R.id.imageView);
        logo_first = findViewById(R.id.logo_first);
        logo_secend = findViewById(R.id.logo_secend);
        boolean result = checkExternalImage();
        if (!result) {

        }

    }

    public void displayVidoe(String name){
        String videoPath = "https://"+BuildConfig.DOMAIN+".com/officefiles/"+name+"/screen/screen2.mp4";
        Uri videoUri = Uri.parse(videoPath);
        alternativeTextView.setVisibility(View.GONE);
        summaryRow.setVisibility(View.GONE);
        fixedTitle.setVisibility(View.GONE);
        fixedImagesContainer.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        videoView.setVideoURI(videoUri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true); // Set looping true to loop the video
            }
        });
        videoView.start();
    }


    public void displayPicture(String name){
       String imagePath = "https://"+BuildConfig.DOMAIN+".com/officefiles/"+name+"/screen/screen2.png";
        imageView = findViewById(R.id.imageView);
        alternativeTextView.setVisibility(View.GONE);
        summaryRow.setVisibility(View.GONE);
        fixedTitle.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        fixedImagesContainer.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        Picasso.get().invalidate(imagePath);
        Picasso.get().load(imagePath).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
    }

    public void displayLogo(String name){
        Log.e("DISPLAY_LOGO", "displayLogo called. name=" + name);

        videoView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        String imagePath1 = "https://"+BuildConfig.DOMAIN+".com/officefiles/"+name+"/screen/logo1.png";
        String imagePath2 = "https://"+BuildConfig.DOMAIN+".com/officefiles/"+name+"/screen/logo2.png";
        Log.d("IMG_URL", imagePath2);
        logo_first = findViewById(R.id.logo_first);
        logo_first.setVisibility(View.VISIBLE);
        Picasso.get().invalidate(imagePath1);
      //  Picasso.get().load(imagePath1).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(logo_first);
        Picasso.get()
                .load(imagePath1)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(logo_first, new com.squareup.picasso.Callback() {
                    @Override public void onSuccess() {
                        Log.d("PICASSO", "Loaded OK");
                    }
                    @Override public void onError(Exception e) {
                        Log.e("PICASSO", "Load failed: " + imagePath1, e);
                    }
                });
        logo_secend = findViewById(R.id.logo_secend);
        logo_secend.setVisibility(View.VISIBLE);
        Picasso.get().invalidate(imagePath2);
       // Picasso.get().load(imagePath2).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(logo_secend);
        Picasso.get()
                .load(imagePath2)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(logo_secend, new com.squareup.picasso.Callback() {
                    @Override public void onSuccess() {
                        Log.d("PICASSO", "Loaded OK");
                    }
                    @Override public void onError(Exception e) {
                        Log.e("PICASSO", "Load failed: " + imagePath2, e);
                    }
                });
    }

    public void updateProducts(List<Product> updatedProducts,double totalAmount) {
        displayProducts(totalAmount);
        if(productList.size()>10){
            scrollToBottom();
        }
    }


    private void displayProducts(double totalAmount) {
        alternativeTextView.setVisibility(View.GONE);
        tableLayout.removeAllViews(); // Clear existing rows

        if (productList == null || productList.isEmpty()) {
            totalPriceTextView.setText(""); // Clear total price
            return;
        }

        int index = 0; // Row index for tracking odd/even rows
        for (Product product : productList) {
            // Create a new ConstraintLayout for each row
            ConstraintLayout row = new ConstraintLayout(context);

            // Set alternating row colors
            if (index % 2 == 0) {
                row.setBackgroundColor(Color.WHITE); // Even rows - white background
            } else {
                row.setBackgroundColor(Color.LTGRAY); // Odd rows - light gray background
            }

            // Create product name TextView with percentage width (40% of the parent)
            TextView productName = new TextView(context);
            productName.setId(View.generateViewId());  // Assign unique ID
            String name = product.getName();
            productName.setPadding(
                    0,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics()),
                    0,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics())
            );
            productName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // Use sp for text size
            productName.setTextColor(Color.BLACK);
            productName.setMaxLines(2);
            productName.setText(wrapText(name, 17));
            productName.setGravity(Gravity.END); // Align text to the right within the view
            productName.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START); // Maintain proper alignment for RTL/LTR text
            productName.setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // Ensure proper layout direction for mixed languages


            // Create product amount TextView with percentage width (30% of the parent)
            TextView productAmount = new TextView(context);
            productAmount.setId(View.generateViewId());  // Assign unique ID
            productAmount.setText(String.valueOf(product.getQuantity()));
            productAmount.setPadding(
                    0,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics()),
                    0,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics())
            );
            productAmount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // Use sp for text size
            productAmount.setTextColor(Color.BLACK);

            // Create product price TextView with percentage width (30% of the parent)
            TextView productPrice = new TextView(context);
            productPrice.setId(View.generateViewId());  // Assign unique ID
            productPrice.setText(String.valueOf(product.getPrice()));
            productPrice.setPadding(
                    0,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics()),
                    0,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics())
            );
            productPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // Use sp for text size
            productPrice.setTextColor(Color.BLACK);
            // Set constraints for product name (40% of the row width)
            ConstraintLayout.LayoutParams productNameParams = new ConstraintLayout.LayoutParams(
                    0, // width set to 0 to use constraint
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            productNameParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            productNameParams.endToStart = productAmount.getId(); // Use productAmount ID here
            productNameParams.setMargins(0, 20, 30, 20);
            productName.setLayoutParams(productNameParams);

            row.addView(productName);

            // Set constraints for product amount (30% of the row width)
            ConstraintLayout.LayoutParams productAmountParams = new ConstraintLayout.LayoutParams(
                    0, // width set to 0 to use constraint
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            productAmountParams.startToEnd = productName.getId(); // Align after the product name
            productAmountParams.endToStart = productPrice.getId(); // Align before the product price
            productAmountParams.setMargins(0, 20, 110, 20);
            productAmount.setLayoutParams(productAmountParams);

            row.addView(productAmount);

            // Set constraints for product price (30% of the row width)
            ConstraintLayout.LayoutParams productPriceParams = new ConstraintLayout.LayoutParams(
                    0, // width set to 0 to use constraint
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            productPriceParams.startToEnd = productAmount.getId(); // Align after the product amount
            productPriceParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID; // Align to the right
            productPriceParams.setMargins(0, 20, 0, 20);
            productPrice.setLayoutParams(productPriceParams);

            row.addView(productPrice);

            // Add the row to the TableLayout
            tableLayout.addView(row);

            // Check if there is a discount for this product
            if (product.getDiscount() > 0) { // Only add a discount row if there's a discount
                ConstraintLayout discountRow = new ConstraintLayout(context);
                discountRow.setBackgroundColor(Color.parseColor("#D0F0C0")); // Light green color for discounts

                TextView discountLabel = new TextView(context);
                discountLabel.setId(View.generateViewId());
                discountLabel.setText(product.getDiscountName());
                discountLabel.setPadding(
                        0,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()),
                        0,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics())
                );
                discountLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // Use sp for text size
                discountLabel.setTextColor(Color.RED);
                discountRow.addView(discountLabel);

                TextView discountValue = new TextView(context);
                discountValue.setId(View.generateViewId());
                discountValue.setText(String.valueOf(product.getDiscount() * -1)); // Discount value (negative)
                discountValue.setPadding(
                        0,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()),
                        0,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics())
                );
                discountValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // Use sp for text size
                discountValue.setTextColor(Color.RED);
                discountRow.addView(discountValue);
                ConstraintLayout.LayoutParams discountLabelParams = new ConstraintLayout.LayoutParams(
                        0, // width set to 0 to use constraint
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                );
                discountLabelParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                discountLabelParams.endToStart = discountValue.getId(); // Align before discountValue
                discountLabelParams.setMargins(0, 10, 30, 10);
                discountLabel.setLayoutParams(discountLabelParams);

                // Set constraints for discountValue (align with productPrice)
                ConstraintLayout.LayoutParams discountValueParams = new ConstraintLayout.LayoutParams(
                        0, // width set to 0 to use constraint
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                );
                discountValueParams.startToEnd = discountLabel.getId(); // Align after discountLabel
                discountValueParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID; // Align to the right
                discountValueParams.setMargins(0, 10, 0, 10);
                discountValue.setLayoutParams(discountValueParams);
                // Add the discount row to the TableLayout
                tableLayout.addView(discountRow);
            }

            index++; // Increment row index
        }

        // Format total price with rounding
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING); // Optional rounding mode
        String formattedTotalPrice = df.format(totalAmount);
        totalPriceTextView.setText(formattedTotalPrice);
    }





    public void removeProduct(int index, double totalprice) {
        for (Iterator<Product> iterator = productList.iterator(); iterator.hasNext();) {
            Product product = iterator.next();
            if (product.getIndex()==(index)) {
                iterator.remove(); // Remove the product from the list
                displayProducts(totalprice); // Update the displayed products after removal
                break; // Exit loop once the product is found and removed
            }
        }
    }

    public void plusCountProdFromPresentation(int index, double price, double totalprice) {
        for (Iterator<Product> iterator = productList.iterator(); iterator.hasNext();) {
            Product product = iterator.next();
            if (product.getIndex()==(index)) {
                product.setQuantity(product.getQuantity() + 1);
                product.setPrice(product.getPrice() + (price));
                break; // Exit loop once the product is found and removed
            }
        }
        displayProducts(totalprice);
    }

    public void minusCountProdFromPresentation(int index, double price, double totalprice) {
        for (Iterator<Product> iterator = productList.iterator(); iterator.hasNext();) {
            Product product = iterator.next();
            if (product.getIndex()==(index)) {
                product.setQuantity(product.getQuantity() - 1);
                product.setPrice(product.getPrice() - (price));
                break; // Exit loop once the product is found and removed
            }
        }
        displayProducts(totalprice);
    }

    public void changeCountProdFromPresentation(int index,int count, double totalpriceprod , double totalprice) {
        for (Iterator<Product> iterator = productList.iterator(); iterator.hasNext();) {
            Product product = iterator.next();
            if (product.getIndex()==(index)) {
                product.setQuantity(count);
                product.setPrice(product.getPrice()+totalpriceprod);
                break; // Exit loop once the product is found and removed
            }
        }
        displayProducts(totalprice);
    }
    public void addAnachaProdFromPresentation(int index,int count, double totalpriceprod,double anacha,double totalprice) {
        for (Iterator<Product> iterator = productList.iterator(); iterator.hasNext();) {
            Product product = iterator.next();
            if (product.getIndex()==(index)) {
                product.setPrice(totalpriceprod);
                product.setAnacha(anacha);
                break; // Exit loop once the product is found and removed
            }
        }
        displayProducts(totalprice);
    }
    public void addDiscountProdFromPresentation( int index,String discountName,double discount,double totalprice) {
        for (Iterator<Product> iterator = productList.iterator(); iterator.hasNext();) {
            Product product = iterator.next();
            if (product.getIndex()==(index)) {
                product.setDiscount(discount);
                product.setDiscountName(discountName);
                break; // Exit loop once the product is found and removed
            }
        }
        displayProducts(totalprice);
    }

    public void addAnachaGeneralFromPresentation(double totalprice) {
        displayProducts(totalprice);
    }

    public void changeTotalPriceProduct( double totalprice) {
//            product.setQuantity(count);
//            product.setPrice(product.getPrice()+totalprice);
        displayProducts(100);
    }

    public void clearDisplayedProducts() {

        if (productList != null) {
            tableLayout.removeAllViews(); // Clear all rows from the table
            productList.clear();
            displayProducts(0);
            alternativeTextView.setVisibility(View.VISIBLE);
        }

// Clear all rows from the table
    }
    private void scrollToBottom() {
        if (scrollView != null && tableLayout != null) {
            // Programmatically scroll the ScrollView to the bottom of the TableLayout
            tableLayout.post(new Runnable() {
                @Override
                public void run() {
                    // Calculate the bottom position of the TableLayout
                    int scrollToY = tableLayout.getBottom() - scrollView.getHeight();

                    // Ensure scrollToY is within valid range
                    if (scrollToY < 0) {
                        scrollToY = 0;
                    }

                    // Scroll the ScrollView to the calculated position
                    scrollView.scrollTo(0, scrollToY);
                }
            });
        }
        else {
            // Handle the case where scrollView is null
           // Log.e("ScrollError", "scrollView is null");
        }
    }

    private boolean loadImage(File imgFile){
        try {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {

//            File imgFile = new File(imagePath);

                String mime = Files.probeContentType(Paths.get(imgFile.getAbsolutePath()));
                if (mime.contains("jpeg") || mime.contains("jpg") || mime.contains("png") || mime.contains("bmp")) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    if (myBitmap == null) return false;
                    img.setImageBitmap(myBitmap);

                    return true;
                } else if (mime.contains("svg")) {
                    FileInputStream stream = new FileInputStream(imgFile.getAbsoluteFile());

                    Sharp.loadInputStream(stream).into(img);
                    stream.close();
                    return true;
                }
                return false;


        }else{
            FileInputStream stream = new FileInputStream(imgFile.getAbsoluteFile());
            Sharp.loadInputStream(stream).into(img);
            stream.close();
        }
        }catch(Exception e){
            return false;
        }
        return false;

    }
    private String wrapText(String text, int maxLineLength) {
        StringBuilder wrappedText = new StringBuilder();
        String[] words = text.split(" ");
        int currentLineLength = 0;

        for (String word : words) {
            if (currentLineLength + word.length() > maxLineLength) {
                wrappedText.append("\n");
                currentLineLength = 0;
            }
            if (currentLineLength > 0) {
                wrappedText.append(" ");
            }
            wrappedText.append(word);
            currentLineLength += word.length() + 1; // Add 1 for the space
        }

        return wrappedText.toString();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (youTubePlayer != null) {
            youTubePlayer.removeListener(you_tube_listener);
            youTubePlayer.pause();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkExternalImage() {

//        playVideo();

        try {
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                // Both Read and write operations available

                File externalDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                boolean result;
                if (externalDir.list().length > 0){

                    result = loadImage(externalDir.listFiles()[0]);
                    if(result)
                        return  true;
                }

                 externalDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS);
                result = loadImage(externalDir.listFiles()[0]);
                if(result)
                    return  true;

           //   playVideo();

            } else {
                return false;
            }
        } catch (Exception e) {

        }
        return  false;
    }

    private void playVideo() {

        img.setVisibility(View.GONE);

        you_tube_player.setVisibility(View.VISIBLE);

        you_tube_player.addYouTubePlayerListener(you_tube_listener);

    }

    AbstractYouTubePlayerListener  you_tube_listener = new AbstractYouTubePlayerListener() {
        @Override
        public void onReady(@NonNull YouTubePlayer _youTubePlayer) {
            String videoId = "orJSJGHjBLI";
            youTubePlayer = _youTubePlayer;
            youTubePlayer.loadVideo(videoId, 0);
            youTubePlayer.setVolume(0);
            youTubePlayer.mute();
            youTubePlayer.play();
        }
    };


}
