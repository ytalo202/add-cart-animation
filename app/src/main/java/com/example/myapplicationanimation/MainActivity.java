package com.example.myapplicationanimation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;

    private ImageView cart;

    private ArrayList<Bitmap> bitmapList = new ArrayList<>();

    private RelativeLayout rl;

    private PathMeasure mPathMeasure;
    MyAdapter myAdapter;
    /**
     * Las coordenadas del punto en el medio de la curva de Bezier
     */

    private float[] mCurrentPosition = new float[2];

    private TextView count;

    private int i = 0;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_baseline_shopping_cart_24);

        ImageView lol = findViewById(R.id.lol);
//        lol.setImageDrawable(bitmap.get);
//        lol.setImageBitmap(bitmap);

//        byte[] decodedString = Base64.decode(
//                "https://w7.pngwing.com/pngs/170/317/png-transparent-re-zero-starting-life-in-another-world-vol-1-light-novel-re-zero-%E2%88%92-starting-life-in-another-world-anime-chibi-kavaii-anime-purple-cg-artwork-black-hair.png",Base64.NO_WRAP);
//        InputStream inputStream  = new ByteArrayInputStream(decodedString);
//        Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
//        lol.setImageBitmap(bitmap);


        // Load a bitmap from the drawable folder
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(), R.drawable.emilia);
// Resize the bitmap to 150x100 (width x height)
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 150, 100, true);
// Loads the resized Bitmap into an ImageView

        lol.setImageBitmap(bMapScaled);


//        lol.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_shopping_cart_24));
//        lol.setImageDrawable(bitmap.);

//        Picasso.get().load("https://w7.pngwing.com/pngs/170/317/png-transparent-re-zero-starting-life-in-another-world-vol-1-light-novel-re-zero-%E2%88%92-starting-life-in-another-world-anime-chibi-kavaii-anime-purple-cg-artwork-black-hair.png").into(lol);
        initView();

        initImg();
//
        myAdapter = new MyAdapter(bitmapList);

        mRecyclerView.setAdapter(myAdapter);
//
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }


    private void initImg() {
        bitmapList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.emilia));
        bitmapList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.emilia2));
        bitmapList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_baseline_sentiment_dissatisfied_24));
        bitmapList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_baseline_sentiment_dissatisfied_24));


    }


    private void initView() {

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        cart = (ImageView) findViewById(R.id.cart);

        rl = (RelativeLayout) findViewById(R.id.rl);

        count = (TextView) findViewById(R.id.count);

    }


    class MyAdapter extends RecyclerView.Adapter<MyVH> {

        private ArrayList<Bitmap> bitmapList;


        public MyAdapter(ArrayList<Bitmap> bitmapList) {

            this.bitmapList = bitmapList;

        }


        @Override
        public MyVH onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);

            View itemView = inflater.inflate(R.layout.item, parent, false);

            MyVH myVH = new MyVH(itemView);

            return myVH;
        }


        @Override
        public void onBindViewHolder(final MyVH holder, final int position) {

            holder.iv.setImageBitmap(bitmapList.get(position));
            holder.buy.setText("Item+" + position);
            holder.buy.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    addCart(holder.iv, position);

                }

            });

        }


        @Override
        public int getItemCount() {

            return bitmapList.size();

        }

    }


    /**
     * ★★★★★ El efecto de animación de agregar productos al carrito de compras ★★★★★
     *
     * @param iv
     */

    private void addCart(ImageView iv, int position) {

// Primero, crea el tema de la ejecución de la animación --- imageview

        // Codifique una nueva vista de imagen, el recurso de imagen es la imagen de la vista de imagen anterior

        // (Esta imagen es la imagen para ejecutar la animación, comenzando desde la posición inicial, pasando por una parábola (curva de Bezier) y moviéndose hacia el carrito de compras)

        final ImageView goods = new ImageView(MainActivity.this);

//        goods.setImageDrawable(iv.getDrawable());
        goods.setImageDrawable(getResources().getDrawable(R.drawable.emilia));


//        RecyclerView.ViewHolder view  = mRecyclerView.findViewHolderForAdapterPosition(position);
        RecyclerView.ViewHolder view = mRecyclerView.findViewHolderForLayoutPosition(position);
        view.itemView.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.itemView.getDrawingCache();
        Bitmap scaledBitmap = scaleDown(bitmap, 100, true);
        goods.setImageDrawable(new BitmapDrawable(getResources(), scaledBitmap));
//        goods.setImageDrawable(view.itemView.g());


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);

        rl.addView(goods, params);


// 2. Preparativos para calcular las coordenadas del punto inicial / final de la animación

        // Obtener las coordenadas del punto de inicio del diseño principal (utilizado para ayudar a calcular las coordenadas del punto al principio / final de la animación)

        int[] parentLocation = new int[2];

        rl.getLocationInWindow(parentLocation);


        // Obtener las coordenadas de la imagen del producto (utilizadas para calcular las coordenadas del inicio de la animación)

        int startLoc[] = new int[2];

        iv.getLocationInWindow(startLoc);


        // Obtener las coordenadas de la imagen del carrito de la compra (se utiliza para calcular las coordenadas después de que finaliza la animación)

        int endLoc[] = new int[2];

        cart.getLocationInWindow(endLoc);


// Tres, oficialmente comienza a calcular las coordenadas del inicio / final de la animación

        // El punto de partida del producto que comenzó a caer: el punto de partida del producto-el punto de partida del diseño principal + la mitad de la imagen del producto

        float startX = startLoc[0] - parentLocation[0] + iv.getWidth() / 2 ;

        float startY = startLoc[1] - parentLocation[1] + iv.getHeight() / 2 - 20;


        // Las coordenadas del punto final después de que cae el producto: el punto de inicio del carrito de compras-el punto de inicio del diseño principal + 1/5 de la imagen del carrito de compras

        float toX = endLoc[0] - parentLocation[0] + cart.getWidth() / 5 -20;

        float toY = endLoc[1] - parentLocation[1];


// Cuatro. Calcula las coordenadas de interpolación (curva de Bezier) de la animación intermedia (de hecho, usa la curva de Bezier para completar el proceso de los puntos inicial y final)

        // Empiece a dibujar la curva de Bezier

        Path path = new Path();

        // Moverse al punto de inicio (el punto de inicio de la curva de Bezier)

        path.moveTo(startX, startY);

        // Utilice la curva cuadrática de Sabel: tenga en cuenta que cuanto mayor sea la primera coordenada inicial, mayor será la distancia lateral de la curva de Bezier. Generalmente, se puede tomar de acuerdo con la siguiente fórmula

        path.quadTo((startX + toX) / 2, startY, toX, toY);

        // mPathMeasure se utiliza para calcular la longitud de la curva de Bezier y las coordenadas de la interpolación media de la curva de Bezier,

        // Si es verdadero, la ruta formará un bucle cerrado

        mPathMeasure = new PathMeasure(path, false);


        // ★★★ Realización de animación de atributos (cálculo de interpolación de 0 a la longitud de la curva de Bezier para obtener el valor de la distancia en el proceso intermedio)

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());

        valueAnimator.setDuration(500);

        // Interpolador lineal de velocidad constante

        valueAnimator.setInterpolator(new LinearInterpolator());

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override

            public void onAnimationUpdate(ValueAnimator animation) {

                // Cuando el cálculo de interpolación esté en progreso, obtenga cada valor en el medio,

                // Aquí este valor es la longitud de la curva en el proceso medio (el valor de la coordenada del punto medio se obtiene de acuerdo con este valor a continuación)

                float value = (Float) animation.getAnimatedValue();

                // ★★★★★ Obtener las coordenadas del punto actual y encapsularlas en mCurrentPosition

                // boolean getPosTan(float distance, float[] pos, float[] tan) ：

                // Pase una distancia distancia (0 <= distancia <= getLength ()) y luego calcule la distancia actual

                // Punto de coordenadas de salida y tangente, pos se llenará automáticamente con coordenadas, este método es muy importante.

                mPathMeasure.getPosTan(value, mCurrentPosition, null);// mCurrentPosition es el valor de la coordenada del punto de distancia intermedia en este momento

                // Las coordenadas de la imagen del producto en movimiento (imagen animada) se establecen en las coordenadas del punto intermedio

                goods.setTranslationX(mCurrentPosition[0]);

                goods.setTranslationY(mCurrentPosition[1]);

            }

        });

// Cinco, inicia la animación

        valueAnimator.start();


// Seis. Procesando después de que termine la animación

        valueAnimator.addListener(new Animator.AnimatorListener() {

            @Override

            public void onAnimationStart(Animator animation) {


            }


            // Cuando termina la animación:

            @Override

            public void onAnimationEnd(Animator animation) {

                // El número de carritos de la compra más 1

                i++;

                count.setText(String.valueOf(i));

                // Elimina la vista de imagen de imagen movida del diseño principal

                rl.removeView(goods);

            }


            @Override

            public void onAnimationCancel(Animator animation) {


            }


            @Override

            public void onAnimationRepeat(Animator animation) {


            }

        });
        Toast.makeText(this, "dfdfssfsf", Toast.LENGTH_SHORT).show();
    }


    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
//        float ratio = Math.min(
//                (float) maxImageSize / realImage.getWidth(),
//                (float) maxImageSize / realImage.getHeight());
//        int width = Math.round((float) ratio * realImage.getWidth());
//        int height = Math.round((float) ratio * realImage.getHeight());

        int width = 200;
        int height = 200;

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }


    class MyVH extends RecyclerView.ViewHolder {


        private ImageView iv;

        private TextView buy;


        public MyVH(View itemView) {

            super(itemView);

            iv = (ImageView) itemView.findViewById(R.id.iv);

            buy = (TextView) itemView.findViewById(R.id.buy);

        }

    }


}