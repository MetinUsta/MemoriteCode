package com.example.memorite.adapter;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.memorite.EntryEditView;
import com.example.memorite.MainActivity;
import com.example.memorite.MemoListView;
import com.example.memorite.R;
import com.example.memorite.model.Entry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Entry> entryArrayList;

    public EntryAdapter(Context context, ArrayList<Entry> entryArrayList){
        this.context = context;
        this.entryArrayList = entryArrayList;
    }

    @NonNull
    @Override
    public EntryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryAdapter.ViewHolder holder, int position) {
        Entry entry = entryArrayList.get(position);
        holder.cardDate.setText(entry.getDate());
        holder.cardOptions.setImageResource(R.drawable.ic_baseline_more_vert_24);
        holder.cardTitle.setText(entry.getTitle());

        if(entry.getImage() != null){
            System.out.println("Not null");
//            Glide.with(context).asBitmap().load(BitmapFactory.decodeFile(entry.getImage())).into(holder.cardImage);
            Glide.with(context).asBitmap().load(entry.getImage()).error(R.drawable.ic_baseline_landscape_24).diskCacheStrategy(DiskCacheStrategy.ALL).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    holder.cardImage.setImageBitmap(resource);
                    holder.cardImage.buildDrawingCache();
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }else{
            System.out.println("Null");
            holder.cardImage.setImageResource(R.drawable.ic_baseline_landscape_24);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(entryArrayList.get(position).getPassword() != null){
                    final EditText textBox = new EditText(context);

// Set the default text to a link of the Queen
                    textBox.setHint("Password");

                    new AlertDialog.Builder(context)
                            .setTitle("Security Control")
                            .setMessage("Please enter the password of the entry")
                            .setView(textBox)
                            .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String pass = textBox.getText().toString();
                                    if(pass.equals(entryArrayList.get(position).getPassword())){
                                        Intent intent = new Intent(context, EntryEditView.class);
                                        intent.putExtra("position", position);
                                        context.startActivity(intent);
                                    }else{
                                        Toast.makeText(context, "You have entered the wrong password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            })
                            .show();
                }else {
                    Intent intent = new Intent(context, EntryEditView.class);
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            }
        });

        holder.cardOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.memolist_menu_popup, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.deleteOption:
                                deleteEntry(position);
                                break;
                            case R.id.shareOption:
                                shareEntry(position);
                                break;
                            case R.id.pdfOption:
                                exportPDF(position);
                                break;
                        }
                        return true;
                    }
                });
            }
        });
    }

    private void exportPDF(int position){
        Entry entry = entryArrayList.get(position);
        PdfDocument pdfDocument = new PdfDocument();

        Paint imagePaint = new Paint();
        Paint textPaint = new Paint();
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        textPaint.setTextSize(20);
        textPaint.setColor(ContextCompat.getColor(context, R.color.black));

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1240, 1754, 1).create();

        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        canvas.drawText(entry.getTitle(), 30, 40, textPaint);

        Bitmap bmp = BitmapFactory.decodeFile(entry.getImage());
        Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, 400, 200, false);
        canvas.drawBitmap(scaledBmp, 30, 100, imagePaint);

        drawTextAndBreakLine(canvas, textPaint, 30, 340, 780, entry.getMemo());

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            String location = geocoder.getFromLocation(entry.getLatitude(), entry.getLongitude(), 1).get(0).getAddressLine(0);
            drawTextAndBreakLine(canvas, textPaint, 30, 820, 400, location);
        } catch (IOException e) {
            e.printStackTrace();
        }

        canvas.drawText(entry.getDate(), 1240 - 150, 40, textPaint);
        
        pdfDocument.finishPage(page);
        
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), entry.getTitle().toString()+".pdf");
        System.out.println(file.getAbsolutePath());
        
        try {
            pdfDocument.writeTo(new FileOutputStream(file));

            Toast.makeText(context, "Successfully exported to PDF.", Toast.LENGTH_SHORT).show();
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA");
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA");
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA");
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA");
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA");
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA");
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA");
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA");
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void drawTextAndBreakLine(final Canvas canvas, final Paint paint,
                                            final float x, final float y, final float maxWidth, final String text) {
        String textToDisplay = text;
        String tempText = "";
        char[] chars;
        float textHeight = paint.descent() - paint.ascent();
        float lastY = y;
        int nextPos = 0;
        int lengthBeforeBreak = textToDisplay.length();
        do {
            lengthBeforeBreak = textToDisplay.length();
            chars = textToDisplay.toCharArray();
            nextPos = paint.breakText(chars, 0, chars.length, maxWidth, null);
            tempText = textToDisplay.substring(0, nextPos);
            textToDisplay = textToDisplay.substring(nextPos, textToDisplay.length());
            canvas.drawText(tempText, x, lastY, paint);
            lastY += textHeight;
        } while(nextPos < lengthBeforeBreak);
    }

    private void deleteEntry(int position){
        entryArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, entryArrayList.size());
    }

    private void shareEntry(int position){
        Intent sendIntent = new Intent();
        Entry entry = entryArrayList.get(position);
        sendIntent.setAction(Intent.ACTION_SEND);
        String message = entry.getTitle() + '\n' + entry.getMemo() + '\n' + entry.getDate();
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(entryArrayList.get(position).getImage()));
        sendIntent.setType("image/jpeg");
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(sendIntent);
    }

    @Override
    public int getItemCount() {
        return entryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView cardImage, cardOptions;
        private TextView cardTitle, cardDate;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cardImage = itemView.findViewById(R.id.cardImage);
            cardOptions = itemView.findViewById(R.id.cardOptions);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardDate = itemView.findViewById(R.id.cardDate);
        }
    }
}
