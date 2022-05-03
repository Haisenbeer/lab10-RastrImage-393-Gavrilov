package com.example.lab10_rastrimage_393_gavrilov;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    //Хранение изображений
    Bitmap bmp1;
    Bitmap bmp2;

    int width, height; //Ширина и высота изображений
    int value; //Свободный параметр

    String nameFile; //Имя файла изображения

    //Массив с названиями операций
    final String[] settingName = new String[]{
            "New image","Copy","Invert","Grey","Black and White", "Invert X", "Invert Y"};

    ImageView img1;
    ImageView img2;

    ListView lst;

    ArrayAdapter<String> adp;

    InputStream stream;

    //393 Gavrilov
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AssetManager asset = getAssets();

        stream = null;
        try {
            stream = asset.open("car.jpg");
        }
        catch (IOException e){
            return;
        }

        bmp1 = BitmapFactory.decodeStream(stream);

        try {
            stream.close();
        }
        catch (IOException e){
            return;
        }

        width = bmp1.getWidth();
        height = bmp1.getHeight();

        bmp2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        img1 = findViewById(R.id.iv1);
        img2 = findViewById(R.id.iv2);

        img1.setImageBitmap(bmp1);
        img2.setImageBitmap(bmp2);

        lst = findViewById(R.id.lst);

        adp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, settingName);

        lst.setAdapter(adp);

        //393 Gavrilov
        lst.setOnItemClickListener((parent, view, position, id) ->
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Setting");

            String curSetting = adp.getItem(position);
            switch (curSetting)
            {
                case "New image":
                    final EditText input = new EditText(this);

                    builder.setView(input);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            nameFile = input.getText().toString();
                            stream = null;

                            try {
                                stream = asset.open(nameFile);
                            }
                            catch (IOException e){
                                Toast.makeText(getApplicationContext(), "File with that name not found",Toast.LENGTH_SHORT).show();
                                return;
                            }

                            bmp1 = BitmapFactory.decodeStream(stream);
                            img1.setImageBitmap(bmp1);

                            try {
                                stream.close();
                            }
                            catch (IOException e){
                                return;
                            }

                            dialog.cancel();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                    break;

                //393 Gavrilov
                case "Copy":
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            int c = bmp1.getPixel(x, y);
                            bmp2.setPixel(x, y, c);
                        }
                    }
                    break;

                case "Invert":
                    for (int x = 0; x < width; x++)
                    {
                        for (int y = 0; y < height; y++)
                        {
                            int c = bmp1.getPixel(x, y);
                            c = Color.argb(255, 255 - Color.red(c), 255 - Color.green(c),255 - Color.blue(c));
                            bmp2.setPixel(x, y, c);
                        }
                    }
                    break;

                case "Grey":
                    for (int x = 0; x < width; x++)
                    {
                        for (int y = 0; y < height; y++)
                        {
                            int sum = bmp1.getPixel(x, y);
                            int r = Color.red(sum);
                            int g = Color.green(sum);
                            int b = Color.blue(sum);

                            sum = (int)((r + g + b) * 0.33);

                            bmp2.setPixel(x, y, Color.argb(255, sum, sum, sum));
                        }
                    }
                    break;

                //393 Gavrilov
                case "Black and White":
                    final SeekBar setting = new SeekBar(this);

                    setting.setMax(255);
                    builder.setView(setting);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            value = setting.getProgress();

                            for (int x = 0; x < width; x++)
                            {
                                for (int y = 0; y < height; y++)
                                {
                                    int sum = bmp1.getPixel(x, y);
                                    int r = Color.red(sum);
                                    int g = Color.green(sum);
                                    int b = Color.blue(sum);

                                    sum = (int)((r + g + b) * 0.33);

                                    if(sum <= value)
                                        sum = 0;
                                    else
                                        sum = 255;

                                    bmp2.setPixel(x, y, Color.argb(255, sum, sum, sum));
                                }
                            }

                            dialog.cancel();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                    break;

                //393 Gavrilov
                case "Invert X":
                    for (int x = 0; x < width; x++)
                    {
                        for (int y = 0; y < height; y++)
                        {
                            int sum = bmp1.getPixel(x, y);

                            bmp2.setPixel(width - x - 1, y, sum);
                        }
                    }
                    break;

                case "Invert Y":
                    for (int x = 0; x < width; x++)
                    {
                        for (int y = 0; y < height; y++)
                        {
                            int sum = bmp1.getPixel(x, y);

                            bmp2.setPixel(x, height - y - 1, sum);
                        }
                    }
                    break;
            }
        });
    }
}