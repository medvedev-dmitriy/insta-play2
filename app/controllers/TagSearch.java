package controllers;


import models.Tag;
import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import play.data.*;
import static play.data.Form.form;

import play.libs.Scala;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;
import static scala.collection.JavaConversions.asScalaBuffer;



/**
 * Created by Dimin on 11.03.2015.
 */
public class TagSearch extends Controller {

    private static final Token EMPTY_TOKEN = null;
    private static Form<Tag> tagForm = form(Tag.class);
    private static String CODE;
    //static Token accessToken;

    public static Result post() {
        Tag tag = tagForm.bindFromRequest().get();
        String tagName = tag.tagName;
        InstagramService service = new InstagramAuthService()
                .apiKey("4b3bf947e41d4bb493635920e5b34a1e")
                .apiSecret("17325634d77c4f9ba380c9c26ce07dc3")
                .callback("http://localhost:8080/handleInstagramToken/")
                .build();
        String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        Verifier verifier = new Verifier(CODE);
        Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
        Instagram instagram = new Instagram(accessToken.getToken(), "17325634d77c4f9ba380c9c26ce07dc3", "127.0.0.1");
        try {
            /* String tagName ="polotsk";*/
            TagMediaFeed mediaFeed = instagram.getRecentMediaTags(tagName);

            List<MediaFeedData> mediaFeeds = mediaFeed.getData();
            List<String> links = new ArrayList<String>();
            for (MediaFeedData link : mediaFeeds){
                links.add(link.getImages().getLowResolution().getImageUrl());
            }

            return ok(views.html.list.render(asScalaBuffer(mediaFeeds)));
        } catch (Exception c) {
            c.getStackTrace();
        }
        return ok(views.html.data.render(Scala.Option(tag)));
    }

    public static Result test(String code) {
        CODE = code;
        InstagramService service = new InstagramAuthService()
                .apiKey("4b3bf947e41d4bb493635920e5b34a1e")
                .apiSecret("17325634d77c4f9ba380c9c26ce07dc3")
                .callback("http://localhost:8080/handleInstagramToken/")
                .build();
        String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        Verifier verifier = new Verifier(code);
        Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
        Instagram instagram = new Instagram(accessToken.getToken(), "17325634d77c4f9ba380c9c26ce07dc3", "127.0.0.1");
        try {
           /* String tagName ="polotsk";*/
            String tagName = tagForm.bindFromRequest().name();
            TagMediaFeed mediaFeed = instagram.getRecentMediaTags(tagName);

            List<MediaFeedData> mediaFeeds = mediaFeed.getData();
            List<String> links = new ArrayList<String>();
            for (MediaFeedData link : mediaFeeds){
                links.add(link.getImages().getLowResolution().getImageUrl());
            }

            /*return ok(views.html.list.render(asScalaBuffer(mediaFeeds)));*/
            return ok(views.html.data.render(Scala.Option((Tag) null)));
        } catch (Exception c) {
            c.getStackTrace();
        }
        return ok(views.html.error.render());
    }
}
