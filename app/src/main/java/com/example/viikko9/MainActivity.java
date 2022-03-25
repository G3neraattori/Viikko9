package com.example.viikko9;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
//implements AdapterView.OnItemSelectedListener
public class MainActivity extends AppCompatActivity {

    EditText text;
    String kello = "";
    int gid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        text = (EditText) findViewById(R.id.dateTime);
        //text.addTextChangedListener(inputTextWatcher);

        ArrayList<Theatre> teatterit;
        teatterit = readXML();
        /*for(int i = 0; i < teatterit.size(); i++){
            System.out.println(teatterit.get(i));
        }*/
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayList<String> nimet = new ArrayList<>();
        for (int i = 0; i < teatterit.size(); i++) {
            nimet.add(teatterit.get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_item, nimet);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                int id = teatterit.get(position).getID();
                System.out.println(id);
                listaa(id);
                gid = id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    /*TextWatcher inputTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            text.setText(s.toString());
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after){
        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };*/

    public ArrayList<Theatre> readXML(){
        ArrayList<Theatre> teatterit = new ArrayList<>();
        try{
            String urlString = "https://www.finnkino.fi/xml/TheatreAreas/";
            DocumentBuilder docB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDoc = docB.parse(urlString);

            xmlDoc.getDocumentElement().normalize();

            System.out.println(xmlDoc.getDocumentElement().getNodeName());
            NodeList nList = xmlDoc.getDocumentElement().getElementsByTagName("TheatreArea");
            for(int i = 0; i < nList.getLength(); i++){
                Node node = nList.item(i);
                Element element = (Element) node;
                Theatre theatre = new Theatre(((Element) node).getElementsByTagName("Name").item(0).getTextContent(), Integer.parseInt(((Element) node).getElementsByTagName("ID").item(0).getTextContent()));
                teatterit.add(theatre);
            }

        }catch (ParserConfigurationException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return teatterit;
    }


    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String teksti = parent.getItemAtPosition(pos).toString();
        Toast.makeText(parent.getContext(), teksti, Toast.LENGTH_SHORT).show();
        System.out.println(parent.getItemAtPosition(pos));
        //listaa();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }*/


    public void button(View v) {
        kello = text.getText().toString();
        listaa(gid);
        System.out.println(kello);

    }

    public void listaa(int id){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println(id);
        Date date = new Date();
        ArrayAdapter<String> adapter;
        ArrayList<String> naytokset=new ArrayList<String>();
        String urlString ="";
        String pvm = kello;
        //En kerennyt tehdä kuin puoliksi. Osaa listaa tietyn päivän esitykset nappia painaessa mutta ei tietylle kellonajalle.
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            System.out.println("this is format");
            formatter.setLenient(false);
            formatter.parse(pvm);
            urlString = "https://www.finnkino.fi/xml/Schedule/?area=" + id + "&dt=" + pvm;

        } catch (ParseException e) {
            e.printStackTrace();
            urlString = "https://www.finnkino.fi/xml/Schedule/?area=" + id + "&dt=" + dateFormat.format(date);
        }


        try{
            DocumentBuilder docB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDoc = docB.parse(urlString);

            xmlDoc.getDocumentElement().normalize();

            System.out.println(xmlDoc.getDocumentElement().getNodeName());
            NodeList nList = xmlDoc.getDocumentElement().getElementsByTagName("Show");
            for(int i = 0; i < nList.getLength(); i++){
                Node node = nList.item(i);
                Element element = (Element) node;
                naytokset.add(((Element) node).getElementsByTagName("Title").item(0).getTextContent());
            }
            ListView lv  =(ListView)findViewById(R.id.listView);
            adapter=new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    naytokset);
            lv.setAdapter(adapter);

        }catch (ParserConfigurationException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }





}