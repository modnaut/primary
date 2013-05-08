<!DOCTYPE html>
<!--
   Copyright (c) 2012-2013. Sencha Inc.
-->
<html>
<head>
    <title>Ext JS Theme Harness</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <!--
    Load all required links and scripts
    -->
    <link rel="stylesheet" type="text/css" href="../../resources/{themeName}/app.css" />
    <script type="text/javascript" src="../../{frameworkPath}/ext-all.js"></script>

    <!--
    Load the Slicer core scripts
    -->
    <script type="text/javascript" src="slice-src/slicer.js"></script>
    <script type="text/javascript" src="slice-src/render.js"></script>

    <!--
    Load all manifests and shortcuts
    -->
    <script type="text/javascript" src="slice-src/manifest.js"></script>
    <script type="text/javascript" src="slice-src/shortcuts.js"></script>
    <script type="text/javascript" src="slice-src/custom.js"></script>

     <style>
        .widget-container {
            margin: 10px;
            width: 400px;
            position: relative;
            overflow: visible;
        }

        .x-slicer-target,
        .x-slicer-target * {
            opacity: 1;
        }
    </style>
</head>
    <body></body>
</html>
