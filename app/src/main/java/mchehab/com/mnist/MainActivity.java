package mchehab.com.mnist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;
    private DrawView drawView;
    private Button buttonClear;
    private Button buttonPredict;

    private TFClassifier tfClassifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tfClassifier = new TFClassifier(getApplicationContext());

        textViewResult = findViewById(R.id.textViewResult);
        drawView = findViewById(R.id.drawView);
        buttonClear = findViewById(R.id.buttonClear);
        buttonPredict = findViewById(R.id.buttonPredict);

        buttonClear.setOnClickListener(e -> drawView.clearCanvas());

        buttonPredict.setOnClickListener(e -> {
            float[] result = tfClassifier.predict(drawView.getPixels());
            float max = -1;
            int answer = -1;
            for(int i=0;i<result.length;i++){
                if(result[i] > max){
                    max = result[i];
                    answer = i;
                }
            }
            textViewResult.setText("Model Prediction: " + answer + "\nConfidence: " + max);
        });
    }
}