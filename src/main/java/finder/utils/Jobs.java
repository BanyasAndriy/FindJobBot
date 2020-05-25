package finder.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import finder.model.Vacancy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Jobs {


    public static List<Vacancy> getJobsFromWorkUa(String vacancy, String city)  {

        List<Vacancy> vacancies = new ArrayList<>();
        String url = "https://www.work.ua/ru/jobs-" + city.toLowerCase() + "-" + vacancy.toLowerCase().replaceAll(" ", "+") + "/";;

        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements h1Element = document.getElementsByAttributeValue("class", "card card-hover card-visited wordwrap job-link");

        h1Element.forEach(el -> {

            if (el.child(0).toString().startsWith("<h2 class")) {
                Element urlElement = el.child(0);
                String vacancyUrl = urlElement.child(0).attr("href");
                String name = urlElement.child(0).attr("title");
                vacancies.add(new Vacancy("https://www.work.ua" + vacancyUrl, name));
            } else {
                Element urlElement = el.child(1);
                String vacancyUrl = urlElement.child(0).attr("href");
                String name = urlElement.child(0).attr("title");
                vacancies.add(new Vacancy("https://www.work.ua" + vacancyUrl, name));
            }
        });

        return vacancies;
    }

    public static List<Vacancy> getJobsFromDou(String vacancy, String city) {

        List<Vacancy> vacancies = new ArrayList<>();
//        String url = "https://jobs.dou.ua/vacancies/?city=" + city + "&search=" + vacancy.toLowerCase().replaceAll(" ", "+") + "/";
        String url = "https://jobs.dou.ua/vacancies/?city=kiev&search=Junior+Java+Developer/";

        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert document != null;
        Elements h1Element = document.getElementsByAttributeValue("class", "vacancy");

        h1Element.forEach(el -> {


            Element urlElement = el.child(0);
            String vacancyUrl = urlElement.child(0).attr("href");
            String name = urlElement.child(0).html();
            vacancies.add(new Vacancy(vacancyUrl, name));

        });

        return vacancies;
    }




}
