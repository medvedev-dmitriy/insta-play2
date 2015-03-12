package controllers;

import com.sun.javafx.fxml.expression.Expression;
import models.Tag;
import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import play.data.Form;
import play.libs.Scala;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

import static play.data.Form.form;
import static play.mvc.Results.ok;
import static scala.collection.JavaConversions.asScalaBuffer;


/**
 * Created by Dimin on 12.03.2015.
 */
public class Data {
    private static final Token EMPTY_TOKEN = null;
    private static Form<Tag> tagForm = form(Tag.class);
    static Token accessToken = null;
    static InstagramService service = new InstagramAuthService()
            .apiKey("4b3bf947e41d4bb493635920e5b34a1e")
            .apiSecret("17325634d77c4f9ba380c9c26ce07dc3")
            .callback("http://localhost:8080/handleInstagramToken/")
            .build();
   static Verifier verifier;
    static String code;
    static Boolean sing=true;

    public static Result test(String CODE){
        code = CODE;
        return ok(views.html.data.render(Scala.Option((Tag) null)));
    }

    public static Result post(){
        Tag tag = tagForm.bindFromRequest().get();
        String tagName = tag.tagName;
        if (sing) {
            verifier = new Verifier(code);
            accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
            sing = false;
        }
        Instagram instagram = new Instagram(accessToken.getToken(), "17325634d77c4f9ba380c9c26ce07dc3", "127.0.0.1");

        try {
           /* String tagName ="polotsk";*/
            TagMediaFeed mediaFeed = instagram.getRecentMediaTags(tagName);

            List<MediaFeedData> mediaFeeds = mediaFeed.getData();
            List<String> links = new ArrayList<String>();
            for (MediaFeedData link : mediaFeeds){
                links.add(link.getImages().getLowResolution().getImageUrl());
            }

            /*return ok(views.html.list.render(asScalaBuffer(mediaFeeds)));*/
            return ok(views.html.list.render(asScalaBuffer(mediaFeeds)));
        } catch (Exception c) {
            c.getStackTrace();
        }
        return ok();
    }
}
