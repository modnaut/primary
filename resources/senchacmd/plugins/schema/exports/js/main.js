/*
 * Copyright (c) 2012. Sencha Inc.
 * 
 * This file contains the JS exporter for the Schema plugin. This export writes
 * Ext JS / Sencha Touch model definitions for each entity in the schema.
 */

//@require ../../../../js/all.js

function main (args) {
    var db = args.db;
    //var sencha = new com.sencha.command.Sencha();

    echo('Yo ' + db.databaseType);

    for (var i = 0, n = db.entities.size(); i < n; ++i) {
        var entity = db.entities.get(i);
        echo('Entity: ' + entity.name);
    }
}
