package com.drc.cslibraryadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class AboutUs extends AppCompatActivity {
    WebView webView;
    WebSettings webSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        setTitle("About Us");
      //  TextView t1 = (TextView)findViewById(R.id.dec1);
        String text = "The department of Computer Science & Engineering is the latest addition to the institution which in started "+
                "the academic year 1999 - 2000. Identification of India as a potential source of computer professionals and keeping in "+
                "mind the employment potential in this sector, the department aims at providing competent professionals to contribute" +
                " its share to this technology boom. The department currently offers both B.Tech and M.Tech degree courses in computer" +
                " science and engineering stream. The academic excellence is reflected in the high placement record, the number of" +
                " students gaining admission for higher studies in prestigious institutions and the number of papers published per year."+
                "\n\nThis Application is developed By DRC\ndrcreation18@gmail.com";
        //t1.setText(text);


        webView= (WebView) findViewById(R.id.webView);
        webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String htmlText = " %s ";
        String myData = "<html><body bgcolor=\"#B2EBF2\" style=\"text-align:justify\">The department of <b>Computer Science & Engineering</b> of <b>NSS College of Engineering</b> is the latest addition to the institution which in started "+
                "the academic year 1999 - 2000. Identification of India as a potential source of computer professionals and keeping in "+
                "mind the employment potential in this sector, the department aims at providing competent professionals to contribute" +
                " its share to this technology boom. The department currently offers both B.Tech and M.Tech degree courses in computer" +
                " science and engineering stream. The academic excellence is reflected in the high placement record, the number of" +
                " students gaining admission for higher studies in prestigious institutions and the number of papers published per year." +
                "<br><br><br>This application is developed By <b>DRC</b><br>drcreations@gmail.com"+
                "</body></Html>";

        webView.loadData(String.format(htmlText,myData),"text/html","utf-8");
    }
}
