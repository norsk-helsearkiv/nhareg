angular.module('templates-app', ['common/http/error-modal-400.tpl.html', 'common/http/error-modal-404.tpl.html', 'common/http/error-modal-500.tpl.html', 'home/home.tpl.html', 'login/login.tpl.html', 'registrering/registrering.tpl.html']);

angular.module("common/http/error-modal-400.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("common/http/error-modal-400.tpl.html",
    "<div class=\"modal-header\">\n" +
    "    <h3 class=\"modal-title\">{{'error.BAD_REQUEST_STATUS_CODE' | translate}}</h3>\n" +
    "</div>\n" +
    "\n" +
    "<div class=\"modal-body\">\n" +
    "    <p>\n" +
    "        {{'error.BAD_REQUEST' | translate}}\n" +
    "    </p>\n" +
    "</div>\n" +
    "\n" +
    "<div class=\"modal-footer\">\n" +
    "    <button class=\"btn btn-default btn-primary\" ng-click=\"ok()\">{{'common.OK' | translate}}</button>\n" +
    "</div>");
}]);

angular.module("common/http/error-modal-404.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("common/http/error-modal-404.tpl.html",
    "<div class=\"modal-header\">\n" +
    "    <h3 class=\"modal-title\">{{'error.NOT_FOUND_STATUS_CODE' | translate}}</h3>\n" +
    "</div>\n" +
    "\n" +
    "<div class=\"modal-body\">\n" +
    "    <p>\n" +
    "        {{message}} {{'error.NOT_FOUND' | translate}}\n" +
    "    </p>\n" +
    "</div>\n" +
    "\n" +
    "<div class=\"modal-footer\">\n" +
    "    <button class=\"btn btn-default btn-primary\" ng-click=\"ok()\">{{'common.OK' | translate}}</button>\n" +
    "</div>");
}]);

angular.module("common/http/error-modal-500.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("common/http/error-modal-500.tpl.html",
    "<div class=\"modal-header\">\n" +
    "    <h3 class=\"modal-title\">{{'error.SERVER_ERROR_STATUS_CODE' | translate}}</h3>\n" +
    "</div>\n" +
    "\n" +
    "<div class=\"modal-body\">\n" +
    "    <p>\n" +
    "        {{'error.SERVER_ERROR'| translate}}\n" +
    "    </p>\n" +
    "</div>\n" +
    "\n" +
    "<div class=\"modal-footer\">\n" +
    "    <button class=\"btn btn-default btn-primary\" ng-click=\"ok()\">{{'common.OK' | translate}}</button>\n" +
    "</div>");
}]);

angular.module("home/home.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("home/home.tpl.html",
    "<nav class=\"navbar navbar-fixed-top\">\n" +
    "    <div class=\"navbar-header\">\n" +
    "      <a class=\"navbar-brand\" href=\"#\">{{ 'common.APP_TITTEL' | translate }}</a>\n" +
    "    </div>\n" +
    "    <div id=\"navbar\" class=\"navbar-collapse collapse\">\n" +
    "      <ul class=\"nav navbar-nav navbar-right\">\n" +
    "        <li><a class=\"loggut\" href=\"\" data-ng-click=\"loggUt()\">{{ 'login.LOGG_UT' | translate }}</a></li>\n" +
    "      </ul>\n" +
    "  </div>\n" +
    "</nav>\n" +
    "\n" +
    "<div class=\"content\">\n" +
    "<div class=\"container-fluid\">\n" +
    "  <div class=\"row\">\n" +
    "    <!-- Avtaleliste -->\n" +
    "    <div class=\"col-sm-3 col-md-2 sidebar\">\n" +
    "      <h4>Avtaler</h4>\n" +
    "      <ul class=\"nav nav-sidebar\">\n" +
    "        <li class=\"active\"><a href=\"#\">Avtale 1<span class=\"sr-only\">(current)</span></a></li>\n" +
    "        <li><a href=\"#\">Avtale 2</a></li>\n" +
    "        <li><a href=\"#\">Avtale 3</a></li>\n" +
    "        <li><a href=\"#\">Avtale 4</a></li>\n" +
    "      </ul>\n" +
    "    </div>\n" +
    "\n" +
    "    <div class=\"col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main\">\n" +
    "      <h2>Pasientsøk</h2>\n" +
    "      <div class=\"row placeholders\">\n" +
    "        <form class=\"input-group\">\n" +
    "          <input type=\"text\" class=\"form-control\" placeholder=\"{{sok}}...\">\n" +
    "          <span class=\"input-group-btn\">\n" +
    "            <button class=\"btn btn-primary\" type=\"button\">{{ 'common.OK' | translate }}</button>\n" +
    "          </span>\n" +
    "        </form>\n" +
    "      </div>\n" +
    "      \n" +
    "      <h2 class=\"sub-header\">Avleveringer</h2>\n" +
    "      <div class=\"table-responsive\">\n" +
    "        <table class=\"table table-striped\">\n" +
    "          <thead>\n" +
    "            <tr>\n" +
    "              <th>ID</th>\n" +
    "              <th>Navn</th>\n" +
    "              <th>Startdato</th>\n" +
    "              <th>Sluttdato</th>\n" +
    "              <th></th>\n" +
    "            </tr>\n" +
    "          </thead>\n" +
    "          <tbody>\n" +
    "            <tr>\n" +
    "              <td>1,001</td>\n" +
    "              <td>Lorem</td>\n" +
    "              <td>ipsum</td>\n" +
    "              <td>dolor</td>\n" +
    "              <td>sit</td>\n" +
    "            </tr>\n" +
    "            <tr>\n" +
    "              <td>1,002</td>\n" +
    "              <td>amet</td>\n" +
    "              <td>consectetur</td>\n" +
    "              <td>adipiscing</td>\n" +
    "              <td>elit</td>\n" +
    "            </tr>\n" +
    "            <tr>\n" +
    "              <td>1,003</td>\n" +
    "              <td>Integer</td>\n" +
    "              <td>nec</td>\n" +
    "              <td>odio</td>\n" +
    "              <td>Praesent</td>\n" +
    "            </tr>\n" +
    "            <tr>\n" +
    "              <td>1,003</td>\n" +
    "              <td>libero</td>\n" +
    "              <td>Sed</td>\n" +
    "              <td>cursus</td>\n" +
    "              <td>ante</td>\n" +
    "            </tr>\n" +
    "            <tr>\n" +
    "              <td>1,004</td>\n" +
    "              <td>dapibus</td>\n" +
    "              <td>diam</td>\n" +
    "              <td>Sed</td>\n" +
    "              <td>nisi</td>\n" +
    "            </tr>\n" +
    "            <tr>\n" +
    "              <td>1,005</td>\n" +
    "              <td>Nulla</td>\n" +
    "              <td>quis</td>\n" +
    "              <td>sem</td>\n" +
    "              <td>at</td>\n" +
    "            </tr>\n" +
    "            <tr>\n" +
    "              <td>1,006</td>\n" +
    "              <td>nibh</td>\n" +
    "              <td>elementum</td>\n" +
    "              <td>imperdiet</td>\n" +
    "              <td>Duis</td>\n" +
    "            </tr>\n" +
    "            <tr>\n" +
    "              <td>1,007</td>\n" +
    "              <td>sagittis</td>\n" +
    "              <td>ipsum</td>\n" +
    "              <td>Praesent</td>\n" +
    "              <td>mauris</td>\n" +
    "            </tr>\n" +
    "            <tr>\n" +
    "              <td>1,008</td>\n" +
    "              <td>Fusce</td>\n" +
    "              <td>nec</td>\n" +
    "              <td>tellus</td>\n" +
    "              <td>sed</td>\n" +
    "            </tr>\n" +
    "          </tbody>\n" +
    "        </table>\n" +
    "      </div>\n" +
    "    </div>\n" +
    "  </div>\n" +
    "</div>\n" +
    "</div>\n" +
    "");
}]);

angular.module("login/login.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("login/login.tpl.html",
    "<div class=\"container\">\n" +
    "\n" +
    "	<form class=\"form-signin\" data-ng-submit=\"submit()\">\n" +
    "		<h2 class=\"form-signin-heading\">{{'login.TITTEL' | translate}}</h2>\n" +
    "\n" +
    "		<div class=\"form-group\">\n" +
    "			<label for=\"inputBrukernavn\" class=\"sr-only\">{{'login.BRUKERNAVN' | translate}}</label>\n" +
    "			<input type=\"text\" id=\"inputBrukernavn\" class=\"form-control\" placeholder=\"{{brukernavn}}...\" autofocus>\n" +
    "		</div>\n" +
    "		\n" +
    "		<div class=\"form-group\">\n" +
    "			<label for=\"inputPassord\" class=\"sr-only\">{{'login.PASSORD' | translate}}</label>\n" +
    "			<input type=\"password\" id=\"inputPassord\" class=\"form-control\" placeholder=\"{{passord}}...\">\n" +
    "		</div>\n" +
    "\n" +
    "		<button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">{{'common.OK' | translate}}</button>\n" +
    "	</form>\n" +
    "</div>\n" +
    "");
}]);

angular.module("registrering/registrering.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("registrering/registrering.tpl.html",
    "<nav class=\"navbar navbar-fixed-top\">\n" +
    "    <div class=\"navbar-header\">\n" +
    "      <a class=\"navbar-brand\" href=\"#\">{{ 'common.APP_TITTEL' | translate }}</a>\n" +
    "    </div>\n" +
    "    <div id=\"navbar\" class=\"navbar-collapse collapse\">\n" +
    "      <ul class=\"nav navbar-nav navbar-right\">\n" +
    "      	<li><a href=\"\" data-ng-click=\"\">Vis hurtigtaster</a></li>\n" +
    "        <li><a class=\"loggut\" href=\"\" data-ng-click=\"loggUt()\">{{ 'login.LOGG_UT' | translate }}</a></li>\n" +
    "      </ul>\n" +
    "  </div>\n" +
    "</nav>\n" +
    "\n" +
    "<div class=\"container content\">\n" +
    "	<h1>Registrering</h1>\n" +
    "	<form>\n" +
    "		<div class=\"well\">\n" +
    "			<legend>Lagringsenhet</legend>\n" +
    "			<div class=\"form-group\">\n" +
    "				<label>Tekstfelt 1:</label>\n" +
    "				<input type=\"text\" class=\"form-control\" placeholder=\"Tekstfelt 1...\" autofocus>\n" +
    "			</div>\n" +
    "\n" +
    "			<div class=\"form-group\">\n" +
    "				<label>Tekstfelt 2:</label>\n" +
    "				<input type=\"text\" class=\"form-control\" placeholder=\"Tekstfelt 2...\" autofocus>\n" +
    "			</div>\n" +
    "		</div>\n" +
    "\n" +
    "		<div class=\"well\">\n" +
    "			<legend>Persondata</legend>\n" +
    "			<div class=\"form-group\">\n" +
    "				<label>Personnummer / ID</label>\n" +
    "				<input type=\"text\" class=\"form-control\" placeholder=\"Tekstfelt 1...\" autofocus>\n" +
    "			</div>\n" +
    "\n" +
    "			<div class=\"form-group\">\n" +
    "				<label>Navn</label>\n" +
    "				<input type=\"text\" class=\"form-control\" placeholder=\"Tekstfelt 2...\" autofocus>\n" +
    "			</div>\n" +
    "		</div>\n" +
    "\n" +
    "		<div class=\"well\">\n" +
    "			<legend>Diagnoser</legend>\n" +
    "		</div>\n" +
    "\n" +
    "		<div class=\"well\">\n" +
    "			<legend>Vedlegg</legend>\n" +
    "		</div>\n" +
    "\n" +
    "		<button type=\"submit\" class=\"btn btn-primary\">Submit</button>\n" +
    "		<button class=\"btn btn-default\">Tilbake</button>\n" +
    "	</form>\n" +
    "</div>");
}]);
