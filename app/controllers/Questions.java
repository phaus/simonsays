package controllers;

import java.util.List;
import models.Question;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;

public class Questions extends Controller {
    public static void index() {
        List<Question> entities = models.Question.find("order by text ASC").fetch();
        render(entities);
    }

    public static void create(Question entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        Question entity = Question.findById(id);
        render(entity);
    }

    public static void edit(java.lang.Long id) {
        Question entity = Question.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        Question entity = Question.findById(id);
        entity.delete();
        index();
    }
    
    public static void save(@Valid Question entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "Question"));
        index();
    }

    public static void update(@Valid Question entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }
        
              entity = entity.merge();
        
        entity.save();
        flash.success(Messages.get("scaffold.updated", "Question"));
        index();
    }
}
