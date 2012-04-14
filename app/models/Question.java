/**
 * Question
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
import javax.persistence.Transient;
import play.Logger;
import play.cache.Cache;
import play.data.binding.As;
import play.db.jpa.JPA;
import play.db.jpa.Model;
import play.libs.Codec;

@Entity
public class Question extends Model {

    @ManyToMany
    public Set<Tag> tags;
    @Lob
    public String text;
    public String hash;
    public boolean answered;
    @As(lang = {"*"}, value = {"yyyy-MM-dd hh:mm:ss"})
    public Date created_at;
    @Transient
    public boolean newQuestion = false;

    public Question() {
        this.created_at = new Date();
        this.tags = new HashSet<Tag>();
    }

    public static Question findOrCreateTagByText(String text, List<Tag> tags) {
        String hash = Codec.hexSHA1(text.trim());
        String key = "question_" + hash;
        Question question = Cache.get(key, Question.class);
        if (question == null) {
            question = Question.find("hash = ?", hash).first();
        }
        if (question == null) {
            question = new Question();
            question.created_at = new Date();
            question.answered = false;
            question.hash = hash;
            question.text = text;
            question.setTags(tags);
            question.newQuestion = true;
            question.save();
            Logger.info("creating Question: " + question.text);
        }
        Cache.set(key, question, "1d");
        return question;
    }

    public void setTags(List<Tag> tags) {
        if (this.tags == null) {
            this.tags = new HashSet<Tag>();
        }
        for (Tag t : tags) {
            this.tags.add(t);
        }
    }

    public static List<Question> getQuestionsForTags(Set<Tag> tags) {
        Query query;
        if (tags.size() > 0) {
            query = JPA.em().createQuery("SELECT DISTINCT q FROM Question q INNER JOIN q.tags t WHERE t IN (:tagList)").setParameter("tagList", tags);
        } else {
            query = JPA.em().createQuery("SELECT DISTINCT q FROM Question q INNER JOIN q.tags t");
        }
        List<Question> questions = query.getResultList();
        return questions;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
