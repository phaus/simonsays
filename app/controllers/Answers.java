package controllers;

import java.util.List;
import models.Answer;
import models.Question;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;

public class Answers extends Controller {
    public static void index() {
        List<Answer> entities = models.Answer.find("order by text ASC").fetch();
        render(entities);
    }

    public static void create(Answer entity) {
        render(entity);
    }

    public static void show(java.lang.Long id) {
        Answer entity = Answer.findById(id);
        render(entity);
    }

    public static void edit(java.lang.Long id) {
        Answer entity = Answer.findById(id);
        render(entity);
    }

    public static void delete(java.lang.Long id) {
        Answer entity = Answer.findById(id);
        entity.delete();
        index();
    }

    public static void giveAnswerForQuestion(Long qid){
        Question question = Question.findById(qid);
        Answer answer = new Answer();
        answer.setTags(question.tags);
        answer.save();
        edit(answer.id);
    }

    public static void save(@Valid Answer entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@create", entity);
        }
        entity.save();
        flash.success(Messages.get("scaffold.created", "Answer"));
        index();
    }

    public static void update(@Valid Answer entity) {
        if (validation.hasErrors()) {
            flash.error(Messages.get("scaffold.validation"));
            render("@edit", entity);
        }
        
              entity = entity.merge();
        
        entity.save();
        flash.success(Messages.get("scaffold.updated", "Answer"));
        index();
    }
}
