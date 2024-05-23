package com.example.VolunteerWebApp.service;

import com.example.VolunteerWebApp.entity.ParsedPost;
import com.example.VolunteerWebApp.model.ParsedPostModel;
import com.example.VolunteerWebApp.repository.ParsedPostRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ParsedPostService {

    private final ParsedPostRepository parsedPostRepository;

    public List<ParsedPostModel> getPritulaFundraisers() throws IOException {
        String pritulaFoundationURL = "https://prytulafoundation.org";
        List<ParsedPostModel> fundraisersList = new ArrayList<>();
        Document document = Jsoup.connect(pritulaFoundationURL).get();
        Elements armyFundraisers = document.select("div.directions-card");
        for (Element element : armyFundraisers) {
            ParsedPostModel parsedPost = ParsedPostModel.builder()
                    .url(pritulaFoundationURL +
                            element.select("a.directions-card__img").first().attribute("href").getValue())
                    .postName(element.select("h3.directions-card__title").first().text())
                    .description(element.select("div.directions-card__description > p").text())
                    .platformName("Pritula")
                    .build();
            fundraisersList.add(parsedPost);
        }
        return fundraisersList;
    }

    public List<ParsedPostModel> getPovernisZhyvymFundraisers() throws IOException {
        String foundationUrl = "https://savelife.in.ua/projects/";
        List<ParsedPostModel> fundraisersList = new ArrayList<>();
        Document document = Jsoup.connect(foundationUrl).get();
        for (Element post : document.select("div.project-wrapper")){
            ParsedPostModel parsedPostModel = ParsedPostModel.builder()
                    .url(post.select("a.project-link").attr("href"))
                    .postName(post.select("h3.link-click").text())
                    .description(post.select("p").text())
                    .platformName("Return Alive")
                    .build();
            fundraisersList.add(parsedPostModel);
        }

        return fundraisersList;
    }

    public List<ParsedPostModel> getUnited24Fundraisers() throws IOException {
        String foundationUrl = "https://u24.gov.ua/uk/projects";
        List<ParsedPostModel> fundraisersList = new ArrayList<>();
        Document document = Jsoup.connect(foundationUrl).get();
        for (Element post : document.select("div.item-wrapper.ng-star-inserted")){
            ParsedPostModel parsedPostModel = ParsedPostModel.builder()
                    .url("https://u24.gov.ua" + post.select("a.ng-star-inserted").attr("href"))
                    .postName(post.select("a.ng-star-inserted").text())
                    .description(post.select("div.text").text())
                    .platformName("United24")
                    .build();
            fundraisersList.add(parsedPostModel);
        }
        return fundraisersList;
    }

    public List<ParsedPostModel> getKSEFundraisers() throws IOException {
        String foundationUrl = "https://foundation.kse.ua/projects/";
        List<ParsedPostModel> fundraisersList = new ArrayList<>();
        Document document = Jsoup.connect(foundationUrl).get();
        for (Element post : document.select("div.columns_wrap")){
            ParsedPostModel parsedPostModel = ParsedPostModel.builder()
                    .url(post.select("div.columns_wrap__left > a").attr("href"))
                    .postName(post.select("h2.ttl-h3").text())
                    .platformName("KSE")
                    .build();
            fundraisersList.add(parsedPostModel);
        }
        return fundraisersList;
    }
    public List<ParsedPostModel> getNovaUkraineFundraisers() throws IOException {
        String foundationUrl = "https://novaukraine.org/project-category/current-projects/";
        List<ParsedPostModel> fundraisersList = new ArrayList<>();
        Document document = Jsoup.connect(foundationUrl).get();
        for (Element post : document.select("article.layout__cards__card")){
            ParsedPostModel parsedPostModel = ParsedPostModel.builder()
                    .url(post.select("a.learn__more").attr("href"))
                    .postName(post.select("h2.entry-title > a").text())
                    .description(post.select("div.entry-content > p").text())
                    .platformName("Nova Ukraine")
                    .build();
            fundraisersList.add(parsedPostModel);
        }
        return fundraisersList;
    }
    private ParsedPost mapParsedPostModelToParsedPostEntity(ParsedPostModel parsedPostModel) {
        return ParsedPost.builder()
                .postName(parsedPostModel.getPostName())
                .description(parsedPostModel.getDescription())
                .url(parsedPostModel.getUrl())
                .platformName(parsedPostModel.getPlatformName())
                .build();
    }

    @Transactional
    public List<ParsedPost> refreshParsedPosts() throws IOException {
        parsedPostRepository.deleteAll();
        List<ParsedPostModel> allPosts = getPritulaFundraisers();
        allPosts.addAll(getPovernisZhyvymFundraisers());
        allPosts.addAll(getUnited24Fundraisers());
        allPosts.addAll(getKSEFundraisers());
        allPosts.addAll(getNovaUkraineFundraisers());
        return parsedPostRepository.saveAll(allPosts.stream()
                .map(this::mapParsedPostModelToParsedPostEntity)
                .collect(Collectors.toList()));
    }

    public List<ParsedPost> getAllParsedPosts() {
        return parsedPostRepository.findAll();
    }
}
