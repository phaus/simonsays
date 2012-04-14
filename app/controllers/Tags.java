package controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import models.Answer;
import models.Question;
import models.Tag;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;

public class Tags extends Controller {
    public static void index() {
        List<Tag> entities = models.Tag.all().fetch();
        render(entities);
    }

    public static void create(Tag entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        Tag entity = Tag.findById(id);
        Set<Tag> tag = new HashSet<Tag>();
        tag.add(entity);
        List<Question> questions = Question.getQuestionsForTags(tag);
        List<Answer> answers = Answer.getAnswersForTags(tag);
        render(entity, questions, answers);
    }

    public static void edit(java.lang.Long id) {
        Tag entity = Tag.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        Tag entity = Tag.findById(id);
        entity.delete();
        index();
    }
    
    public static void save(@Valid Tag entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "Tag"));
        index();
    }

    public static void update(@Valid Tag entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }
        
              entity = entity.merge();
        
        entity.save();
        flash.success(Messages.get("scaffold.updated", "Tag"));
        index();
    }
}
