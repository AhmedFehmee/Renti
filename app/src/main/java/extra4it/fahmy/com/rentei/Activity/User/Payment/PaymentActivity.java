package extra4it.fahmy.com.rentei.Activity.User.Payment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import extra4it.fahmy.com.rentei.R;

/**
 * Created by Fehoo on 2/24/2018.
 */

public class PaymentActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.visa_payment)
    ImageView visaPayment;
    @BindView(R.id.master_payment)
    ImageView masterPayment;
    @BindView(R.id.pay_pal_payment)
    ImageView payPalPayment;
    @BindView(R.id.sadad_payment)
    ImageView sadadPayment;
    @BindView(R.id.visa_fragment)
    FrameLayout visaFragment;
    @BindView(R.id.master_fragment)
    FrameLayout masterFragment;
    @BindView(R.id.pay_pal_fragment)
    FrameLayout payPalFragment;
    @BindView(R.id.sdad_fragment)
    FrameLayout sdadFragment;
    @BindView(R.id.btn_pay)
    Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#6ad465"));
        }
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        matrix = new ColorMatrix();
        matrix.setSaturation(0);

        filter = new ColorMatrixColorFilter(matrix);
    }

    ColorMatrixColorFilter filter;
    ColorMatrix matrix;

    @OnClick({R.id.visa_payment, R.id.master_payment, R.id.pay_pal_payment, R.id.sadad_payment, R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.visa_payment:
                visaPayment.setImageResource(R.drawable.payment_visa);
                visaPayment.clearColorFilter();
                masterPayment.setColorFilter(filter);
                payPalPayment.setColorFilter(filter);
                sadadPayment.setColorFilter(filter);
                break;
            case R.id.master_payment:
                masterPayment.setImageResource(R.drawable.payment_master);
                masterPayment.clearColorFilter();
                visaPayment.setColorFilter(filter);
                payPalPayment.setColorFilter(filter);
                sadadPayment.setColorFilter(filter);
                break;
            case R.id.pay_pal_payment:
                payPalPayment.setImageResource(R.drawable.payment_pay_pal);
                payPalPayment.clearColorFilter();
                masterPayment.setColorFilter(filter);
                visaPayment.setColorFilter(filter);
                sadadPayment.setColorFilter(filter);
                break;
            case R.id.sadad_payment:
                sadadPayment.setImageResource(R.drawable.payment_sdad);
                sadadPayment.clearColorFilter();
                masterPayment.setColorFilter(filter);
                payPalPayment.setColorFilter(filter);
                visaPayment.setColorFilter(filter);
                break;
            case R.id.btn_pay:
                startActivity(new Intent(PaymentActivity.this, PaymentFinalActivity.class));
                break;
        }
    }
}
