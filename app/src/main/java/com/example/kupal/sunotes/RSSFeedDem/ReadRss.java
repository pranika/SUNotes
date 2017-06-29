package com.example.kupal.sunotes.RSSFeedDem;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by kupal on 3/28/2017.
 */


public class ReadRss extends AsyncTask<Void, Void, Void> {
    Context context;
    String address = "https://news.syr.edu/feed/";
    ProgressDialog progressDialog;
    ArrayList<FeedItem> feedItems;
    RecyclerView recyclerView;
    URL url;
    public ReadRss(Context context, RecyclerView recyclerView) {
        this.recyclerView=recyclerView;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        MyAdapter adapter=new MyAdapter(context,feedItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalSpace(50));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected Void doInBackground(Void... params) {
        ProcessXml(Getdata());

        return null;
    }

    private void ProcessXml(Document data) {
        if (data != null) {
            feedItems=new ArrayList<>();
            Element root = data.getDocumentElement();

            Node channel = root.getChildNodes().item(1);
            NodeList items = channel.getChildNodes();
            for (int i = 0; i < items.getLength(); i++) {
                Node cureentchild = items.item(i);
                if (cureentchild.getNodeName().equalsIgnoreCase("item")) {
                    FeedItem item=new FeedItem();
                    NodeList itemchilds = cureentchild.getChildNodes();
                    for (int j = 0; j < itemchilds.getLength(); j++) {
                        Node cureent = itemchilds.item(j);
                        if (cureent.getNodeName().equalsIgnoreCase("title")){
                            if(cureent.getTextContent()!=null) {
                                item.setTitle(cureent.getTextContent());
                            }
                        }else if (cureent.getNodeName().equalsIgnoreCase("description")){
                            if(cureent.getTextContent()!=null) {
                                item.setDescription(cureent.getTextContent());
                            }
                        }else if (cureent.getNodeName().equalsIgnoreCase("pubDate")){
                            if(cureent.getTextContent()!=null) {
                                item.setPubDate(cureent.getTextContent());
                            }
                        }else if (cureent.getNodeName().equalsIgnoreCase("link")){
                            if(cureent.getTextContent()!=null) {
                                item.setLink(cureent.getTextContent());
                            }
                        }else if (cureent.getNodeName().equalsIgnoreCase("content:encoded")){
                            //this will return us thumbnail url
                            //String value = (String) cureent.getChildNodes().item(0).getTextContent();
                            if(cureent.getTextContent()!=null){
                                String value = (String) cureent.getChildNodes().item(0).getBaseURI();
                                org.jsoup.nodes.Document docHtml = Jsoup.parse(cureent.getTextContent());
                                //select images by tag
                                Elements imgEle = docHtml.select("img");
                                //get src attribute
                                String src = imgEle.attr("src");
                                String url = "https://news.syr.edu/wp-content/uploads/2017/03/RS492351_1SU_2304-1024x682.jpg";
                                item.setThumbnailUrl(src);
                            }
                        }
                    }
                    feedItems.add(item);
                }
            }
        }
    }



    public Document Getdata() {
        try {
            url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDoc = builder.parse(inputStream);
            return xmlDoc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
