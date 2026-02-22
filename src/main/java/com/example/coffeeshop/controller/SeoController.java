package com.example.coffeeshop.controller;

import com.example.coffeeshop.service.CoffeeBeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class SeoController {

    @Autowired
    private CoffeeBeanService coffeeBeanService;

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String getSitemap() {
        String baseUrl = "https://ijocoffee.com.ua";
        String lastMod = LocalDate.now().toString();

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

        // Головна
        xml.append(createUrlTag(baseUrl + "/", "1.0", lastMod));
        // Каталог
        xml.append(createUrlTag(baseUrl + "/coffee", "0.9", lastMod));

        // Всі товари динамічно
        coffeeBeanService.findAll().forEach(coffee -> {
            xml.append(createUrlTag(baseUrl + "/coffee/" + coffee.getId(), "0.8", lastMod));
        });

        xml.append("</urlset>");
        return xml.toString();
    }

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getRobots() {
        return "User-agent: *\n" +
                "Allow: /\n" +
                "Disallow: /admin/\n" +
                "Disallow: /cart/\n" +
                "Disallow: /order/checkout\n" +
                "\n" +
                "Sitemap: https://ijocoffee.com.ua/sitemap.xml";
    }

    private String createUrlTag(String url, String priority, String lastMod) {
        return "<url>" +
                "<loc>" + url + "</loc>" +
                "<lastmod>" + lastMod + "</lastmod>" +
                "<changefreq>daily</changefreq>" +
                "<priority>" + priority + "</priority>" +
                "</url>";
    }
}