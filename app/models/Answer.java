/**
 * Answer
 * 13.04.2012
 * @author Philipp Haussleiter
 *
 */
package models;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Query;
import play.Logger;
import play.cache.Cache;
import play.data.binding.As;
import play.db.jpa.JPA;
import play.db.jpa.Model;
import play.libs.Codec;

@Entity
public class Answer extends Model {

    @ManyToMany
    public Set<Tag> tags;
    @Lob
    public String text;
    public String hash;
    @As(lang = {"*"}, value = {"yyyy-MM-dd hh:mm:ss"})
    public Date created_at;

    public Answer() {
        this.created_at = new Date();
        this.tags = new HashSet<Tag>();
    }

    public Answer(String text) {
        this.created_at = new Date();
        this.tags = new HashSet<Tag>();
    }

    public void setText(String text) {
        for (Tag tag : Tag.findOrCreateTagsForText(text)) {
            this.tags.add(tag);
        }
        this.text = text;
    }

    public static Answer findOrCreateTagByText(String text, Set<Tag> tags) {
        String hash = Codec.hexSHA1(text);
        String key = "answer_" + hash;
        Answer answer = Cache.get(key, Answer.class);
        if (answer == null) {
            answer = Answer.find("hash = ?", hash).first();
        }
        if (answer == null) {
            answer = new Answer(text);
            answer.created_at = new Date();
            answer.setTags(tags);
            answer.hash = hash;
            answer.save();
        }
        Cache.set(key, answer, "1d");
        return answer;
    }

    public void setTags(Set<Tag> tags) {
        if (this.tags == null) {
            this.tags = new HashSet<Tag>();
        }
        for (Tag t : tags) {
            this.tags.add(t);
        }
    }

    public static List<Answer> getAnswersForTags(Set<Tag> tags) {
        Query query;
        if (tags.size() > 0) {
            query = JPA.em().createQuery("SELECT DISTINCT a FROM Answer a INNER JOIN a.tags t WHERE t IN (:tagList)").setParameter("tagList", tags);
        } else {
            query = JPA.em().createQuery("SELECT DISTINCT a FROM Answer a INNER JOIN a.tags t");
        }
        List<Answer> answers = query.getResultList();
        return answers;
    }

    @Override
    public String toString() {
        return this.text;
    }

    public String getTagsAsString() {
        return Tag.tagsToNames(this.tags);
    }
}
