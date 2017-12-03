package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.SmoothieDao;
import tikape.runko.domain.Smoothie;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.domain.RaakaAine;
import tikape.runko.database.SmoothieRaakaAineDao;
import java.util.Collections;
import tikape.runko.domain.SmoothieRaakaAine;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:smoothiearkisto.db");
        database.init();

        SmoothieDao smoothieDao = new SmoothieDao(database);
        RaakaAineDao raakaAineDao = new RaakaAineDao(database);
        SmoothieRaakaAineDao smoothieRaakaAineDao = new SmoothieRaakaAineDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/smoothiet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("smoothiet", smoothieDao.findAll());

            return new ModelAndView(map, "smoothiet");
        }, new ThymeleafTemplateEngine());

        get("/raakaAineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaAineet", raakaAineDao.findAll());

            return new ModelAndView(map, "raakaAineet");
        }, new ThymeleafTemplateEngine());
        get("/raakaAineet/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaAine", raakaAineDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "raakaAine");
        }, new ThymeleafTemplateEngine());

        get("/smoothiet/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int key = Integer.parseInt(req.params(":id"));
            map.put("smoothie", smoothieDao.findOne(key));
            List<SmoothieRaakaAine> raakaAineet = smoothieRaakaAineDao.findAllWhereSmoothieIs(key);
            map.put("valittavatRaakaAineet", raakaAineDao.findAll());
            //Collections.sort(raakaAineet); En saanut toimimaan
            map.put("raakaAineet", raakaAineet);
            return new ModelAndView(map, "smoothie");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/smoothiet/:id", (req, res) -> {
            SmoothieRaakaAine sra = new SmoothieRaakaAine(new RaakaAine(
                    Integer.parseInt(req.params("raakaAine.id")),
                    req.params("raakaAine.nimi")),
                    Integer.parseInt(req.params("jarjestys")),
                    Integer.parseInt(req.params("maara")), req.params("ohje"));

            smoothieRaakaAineDao.saveOrUpdate(sra, Integer.parseInt(req.params(":id")));
            res.redirect("/smoothiet/:id");
            return "";
        });

        get("/luo", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("smoothiet", smoothieDao.findAll());
            map.put("raakaAineet", raakaAineDao.findAll());
            return new ModelAndView(map, "luo");
        }, new ThymeleafTemplateEngine());

        Spark.post("/smoothiet", (req, res) -> {
            Smoothie smoothie = new Smoothie(-1, req.queryParams("nimi"));
            smoothieDao.saveOrUpdate(smoothie);
            res.redirect("/luo");
            return "";
        });
        Spark.post("/raakaAineet", (req, res) -> {
            RaakaAine raakaAine = new RaakaAine(-1, req.queryParams("nimi"));
            raakaAineDao.saveOrUpdate(raakaAine);
            res.redirect("/luo");
            return "";
        });

        Spark.post("/smoothiet/:id/poista", (req, res) -> {
            Integer smoothie = Integer.parseInt(req.queryParams("smoothieId"));
            smoothieDao.delete(smoothie);
            smoothieRaakaAineDao.deleteByAnnos(smoothie);
            res.redirect("/luo");
            return "";
        });

        Spark.post("/raakaAineet/:id/poista", (req, res) -> {
            Integer raakaAine = Integer.parseInt(req.queryParams("raakaAineId"));
            raakaAineDao.delete(raakaAine);
            smoothieRaakaAineDao.deleteByRaakaAine(raakaAine);
            res.redirect("/luo");
            return "";
        });
    }
}
