/*
This file is part of Ext JS 5.0.0.970

Copyright (c) 2011-2014 Sencha Inc

Contact:  http://www.sencha.com/contact

Pre-release code in the Ext repository is intended for development purposes only and will
not always be stable. 

Use of pre-release code is permitted with your application at your own risk under standard
Ext license terms. Public redistribution is prohibited.

For early licensing, please contact us at licensing@sencha.com

Version: 5.0.0.970 Build date: 2014-06-01 09:03:15 (660862da42511e0f769c1d37b9fd8e156edd73eb)

*/
/**
 * Load the library located at the same path with this file
 *
 * Will automatically load ext-all-dev.js if any of these conditions is true:
 * - Current hostname is localhost
 * - Current hostname is an IP v4 address
 * - Current protocol is "file:"
 *
 * Will load ext-all.js (minified) otherwise
 */
(function() {
     var scripts = document.getElementsByTagName('script'),
        isDevelopment = false,
        queryString = window.location.search,
        test, path, i, ln, scriptSrc, match;
    for (i = 0, ln = scripts.length; i < ln; i++) {
        scriptSrc = scripts[i].src;
        match = scriptSrc.match(/bootstrap\.js$/);
        if (match) {
            path = scriptSrc.substring(0, scriptSrc.length - match[0].length);
            break;
        }
    }

	//加载extjs
    if (queryString.match('(\\?|&)debug') !== null) {
        isDevelopment = true;
    }
    document.write('<script type="text/javascript" charset="UTF-8" src="' + 
        path + 'ext-all' + (isDevelopment ? '-debug' : '') + '.js"></script>');
		
	//加载css
	var theme = "classic";
	theme = 'ext-theme-' + theme;
    var packagePath = path + '/packages/' + theme + '/build/';
    var themePath = packagePath + 'resources/' + theme + '-all';
	document.write('<link rel="stylesheet" type="text/css" href="' +
                            themePath + (isDevelopment ? '-debug' : '') + '.css"/>');	
})();