angular.module('templates-app', ['common/http-service/error-modal-400.tpl.html', 'common/http-service/error-modal-404.tpl.html', 'common/http-service/error-modal-500.tpl.html', 'common/http/error-modal-400.tpl.html', 'common/http/error-modal-404.tpl.html', 'common/http/error-modal-500.tpl.html', 'common/list-view/list-view.tpl.html', 'common/modal-service/delete-modal.tpl.html', 'common/modal-service/ny-avlevering.tpl.html', 'common/modal-service/ny-avtale.tpl.html', 'home/home.tpl.html', 'login/login.tpl.html', 'registrering/registrering.tpl.html']);

angular.module("common/http-service/error-modal-400.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("common/http-service/error-modal-400.tpl.html",
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

angular.module("common/http-service/error-modal-404.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("common/http-service/error-modal-404.tpl.html",
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

angular.module("common/http-service/error-modal-500.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("common/http-service/error-modal-500.tpl.html",
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

angular.module("common/list-view/list-view.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("common/list-view/list-view.tpl.html",
    "<nav class=\"navbar navbar-fixed-top\">\n" +
    "    <div class=\"navbar-header\">\n" +
    "      <a class=\"navbar-brand\" href=\"\" data-ng-click=\"navHome()\">{{ 'common.APP_TITTEL' | translate }}</a>\n" +
    "    </div>\n" +
    "    <div id=\"navbar\" class=\"navbar-collapse collapse\">\n" +
    "      <ul class=\"nav navbar-nav navbar-right\">\n" +
    "        <li><a class=\"loggut\" href=\"\" data-ng-click=\"navLoggut()\">{{ 'login.LOGG_UT' | translate }}</a></li>\n" +
    "      </ul>\n" +
    "  </div>\n" +
    "</nav>\n" +
    "\n" +
    "<div class=\"content\">\n" +
    "    <div class=\"container\">\n" +
    "        <h2>{{ tittel.tittel }} <small> {{ tittel.underTittel }}</small></h2>\n" +
    "        <div class=\"table-responsive\">\n" +
    "            <table class=\"table table-striped\">\n" +
    "                <thead>\n" +
    "                    <tr>\n" +
    "                        <th>ID</th>\n" +
    "                        <th>Navn</th>\n" +
    "                    </tr>\n" +
    "                </thead>\n" +
    "                <tbody>\n" +
    "                    <tr class=\"clickable\" data-ng-repeat=\"pasient in data.pasientjournal\" data-ng-click=\"actionVisJournal(pasient.personnummer)\">\n" +
    "                        <td>{{ pasient.personnummer }}</td>\n" +
    "                        <td>{{ pasient.navn }}</td>\n" +
    "                    </tr>\n" +
    "                </tbody>\n" +
    "            </table>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>\n" +
    "");
}]);

angular.module("common/modal-service/delete-modal.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("common/modal-service/delete-modal.tpl.html",
    "<div class=\"modal-header\">\n" +
    "    <h3 class=\"modal-title\">{{'modal.delete.TITTEL' | translate}}</h3>\n" +
    "</div>\n" +
    "\n" +
    "<div class=\"modal-body\">\n" +
    "    <p>\n" +
    "        {{'modal.delete.SIKKER' | translate}} <strong>{{ elementType }}</strong> {{'modal.delete.MED_ID' | translate}} <strong>{{ id }}</strong> ?\n" +
    "    </p>\n" +
    "</div>\n" +
    "\n" +
    "<div class=\"modal-footer\">\n" +
    "    <button class=\"btn btn-default btn-primary\" ng-click=\"ok()\">{{'common.OK' | translate}}</button>\n" +
    "    <button class=\"btn btn-default btn-danger\" ng-click=\"avbryt()\">{{'common.AVBRYT' | translate}}</button>\n" +
    "</div>");
}]);

angular.module("common/modal-service/ny-avlevering.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("common/modal-service/ny-avlevering.tpl.html",
    "<div class=\"modal-header\">\n" +
    "    <h3 class=\"modal-title\">{{'modal.legg_til.TITTEL' | translate}}</h3>\n" +
    "</div>\n" +
    "\n" +
    "<div class=\"modal-body\">\n" +
    "	<form>\n" +
    "		<div class=\"form-group\">\n" +
    "			<label>ID:</label>\n" +
    "			<input type=\"text\" class=\"form-control\" ng-model=\"formData.avleveringsidentifikator\">\n" +
    "			<label class=\"label-error\" data-ng-show=\"formData.error.avleveringsidentifikator\">{{formData.error.avleveringsidentifikator}}</label>\n" +
    "		</div>\n" +
    "		<div class=\"form-group\">\n" +
    "			<label>Arkivskaper:</label>\n" +
    "			<input type=\"text\" class=\"form-control\" ng-model=\"formData.arkivskaper\">\n" +
    "			<label class=\"label-error\" data-ng-show=\"formData.error.arkivskaper\">{{formData.error.arkivskaper}}</label>\n" +
    "		</div>\n" +
    "		<div class=\"form-group\">\n" +
    "			<label>Beskrivelse:</label>\n" +
    "			<input type=\"text\" class=\"form-control\" ng-model=\"formData.avleveringsbeskrivelse\">\n" +
    "			<label class=\"label-error\" data-ng-show=\"formData.error.avleveringsbeskrivelse\">{{formData.error.avleveringsbeskrivelse}}</label>\n" +
    "		</div>\n" +
    "		<button type=\"submit\" data-ng-show=\"false\" data-ng-click=\"ok()\"></div>\n" +
    "	</form>\n" +
    "</div>\n" +
    "\n" +
    "<div class=\"modal-footer\">\n" +
    "    <button class=\"btn btn-default btn-primary\" ng-click=\"ok()\">{{'common.OK' | translate}}</button>\n" +
    "    <button class=\"btn btn-default btn-danger\" ng-click=\"avbryt()\">{{'common.AVBRYT' | translate}}</button>\n" +
    "</div>");
}]);

angular.module("common/modal-service/ny-avtale.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("common/modal-service/ny-avtale.tpl.html",
    "<div class=\"modal-header\">\n" +
    "    <h3 class=\"modal-title\">{{'modal.legg_til.TITTEL' | translate}}</h3>\n" +
    "</div>\n" +
    "\n" +
    "<div class=\"modal-body\">\n" +
    "	<form>\n" +
    "		<div class=\"form-group\">\n" +
    "			<label>ID:</label>\n" +
    "			<input type=\"text\" class=\"form-control\" ng-model=\"formData.avtaleidentifikator\">\n" +
    "			<label class=\"label-error\" data-ng-show=\"formData.error.avtaleidentifikator\">{{formData.error.avtaleidentifikator}}</label>\n" +
    "		</div>\n" +
    "		<div class=\"form-group\">\n" +
    "			<label>Beskrivelse:</label>\n" +
    "			<input type=\"text\" class=\"form-control\" ng-model=\"formData.avtalebeskrivelse\">\n" +
    "			<label class=\"label-error\" data-ng-show=\"formData.error.avtalebeskrivelse\">{{formData.error.avtalebeskrivelse}}</label>\n" +
    "		</div>\n" +
    "		<div class=\"form-group\">\n" +
    "			<label>Dato:</label>\n" +
    "			<input type=\"date\" class=\"form-control\" ng-model=\"formData.avtaledato\">\n" +
    "			<label class=\"label-error\" data-ng-show=\"formData.error.avtaledato\">{{formData.error.avtaledato}}</label>\n" +
    "		</div>\n" +
    "		<button type=\"submit\" data-ng-show=\"false\" data-ng-click=\"ok()\"></div>\n" +
    "	</form>\n" +
    "</div>\n" +
    "\n" +
    "<div class=\"modal-footer\">\n" +
    "    <button class=\"btn btn-default btn-primary\" ng-click=\"ok()\">{{'common.OK' | translate}}</button>\n" +
    "    <button class=\"btn btn-default btn-danger\" ng-click=\"avbryt()\">{{'common.AVBRYT' | translate}}</button>\n" +
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
    "      <h4>\n" +
    "        {{ 'home.AVTALER' | translate }}\n" +
    "        <button class=\"right btn btn-small btn-success\" data-ng-click=\"actionLeggTilAvtale()\">{{ 'common.LEGG_TIL' | translate }}</button>\n" +
    "      </h4>\n" +
    "      <ul class=\"nav nav-sidebar\">\n" +
    "        <li data-ng-repeat=\"avtale in avtaler | orderBy: 'avtalebeskrivelse'\"\n" +
    "            data-ng-click=\"setValgtAvtale(avtale)\"\n" +
    "            data-ng-class=\"{ active: valgtAvtale.avtaleidentifikator === avtale.avtaleidentifikator}\">\n" +
    "            <a href=\"\"><button class=\"icon icon-padding-right icon-delete\" data-ng-click=\"actionDeleteAvtale(text.avtale, avtale.avtaleidentifikator, avtale)\" tooltip=\"{{text.tooltip.deleteElement}}\"></button>{{ avtale.avtalebeskrivelse }}</a>\n" +
    "        </li>\n" +
    "      </ul>\n" +
    "    </div>\n" +
    "\n" +
    "    <div class=\"col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main\">\n" +
    "      <h2>{{ text.pasientsok }}</h2>\n" +
    "      <div class=\"row placeholders\">\n" +
    "        <form class=\"input-group\">\n" +
    "          <input type=\"text\" data-ng-model=\"input\" class=\"form-control\" placeholder=\"{{text.pasientsok}}...\">\n" +
    "          <span class=\"input-group-btn\">\n" +
    "            <button class=\"btn btn-primary\" type=\"submit\" data-ng-click=\"actionSok(input)\">{{ 'common.OK' | translate }}</button>\n" +
    "          </span>\n" +
    "        </form>\n" +
    "      </div>\n" +
    "      \n" +
    "      <h2 class=\"sub-header\">{{ 'home.AVLEVERINGER' | translate }}<small> {{ 'home.FOR' | translate }} {{ valgtAvtale.avtalebeskrivelse }}</small><button class=\"right btn btn-success\" data-ng-click=\"actionLeggTilAvlevering()\">{{ 'common.LEGG_TIL' | translate }}</button></h2>\n" +
    "      <div class=\"table-responsive\">\n" +
    "        <table class=\"table table-striped\">\n" +
    "          <thead>\n" +
    "            <tr>\n" +
    "              <th>{{ 'common.table.ID' | translate }}</th>\n" +
    "              <th>{{ 'common.table.ARKIVSKAPER' | translate }}</th>\n" +
    "              <th>{{ 'common.table.BESKRIVELSE' | translate }}</th>\n" +
    "              <th></th>\n" +
    "            </tr>\n" +
    "          </thead>\n" +
    "          <tbody>\n" +
    "            <tr data-ng-repeat=\"avlevering in avleveringer\">\n" +
    "              <td>{{ avlevering.avleveringsidentifikator }}</td>\n" +
    "              <td>{{ avlevering.arkivskaper }}</td>\n" +
    "              <td>{{ avlevering.avleveringsbeskrivelse }}</td>\n" +
    "              <td>\n" +
    "                <button class=\"icon icon-list\" data-ng-click=\"actionVisAvlevering(avlevering)\" tooltip=\"{{text.tooltip.list}}\"></button>\n" +
    "                <button class=\"icon icon-padding-left icon-add-journal\" data-ng-click=\"actionAdd()\" tooltip=\"{{text.tooltip.add}}\"></button>\n" +
    "                <button class=\"icon icon-padding-left icon-folder\" data-ng-click=\"actionFolder(avlevering.avleveringsidentifikator)\" tooltip=\"{{text.tooltip.folder}}\"></button>\n" +
    "                <button class=\"icon icon-padding-left icon-delete\" data-ng-click=\"actionFjernAvlevering(text.avlevering, avlevering.avleveringsidentifikator, avlevering)\" tooltip=\"{{text.tooltip.deleteElement}}\"></button>\n" +
    "              </td>\n" +
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
    "<div class=\"login\">\n" +
    "	<div class=\"login-form\">\n" +
    "\n" +
    "		<form class=\"form-signin\" data-ng-submit=\"submit()\">\n" +
    "			<h2 class=\"form-signin-heading\">{{'login.TITTEL' | translate}}</h2>\n" +
    "\n" +
    "			<div class=\"form-group\">\n" +
    "				<label for=\"inputBrukernavn\" class=\"sr-only\">{{'login.BRUKERNAVN' | translate}}</label>\n" +
    "				<input type=\"text\" id=\"inputBrukernavn\" class=\"form-control\" placeholder=\"{{brukernavn}}...\" autofocus>\n" +
    "			</div>\n" +
    "			\n" +
    "			<div class=\"form-group\">\n" +
    "				<label for=\"inputPassord\" class=\"sr-only\">{{'login.PASSORD' | translate}}</label>\n" +
    "				<input type=\"password\" id=\"inputPassord\" class=\"form-control\" placeholder=\"{{passord}}...\">\n" +
    "			</div>\n" +
    "\n" +
    "			<button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">{{'common.OK' | translate}}</button>\n" +
    "		</form>\n" +
    "	</div>\n" +
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
    "      	<li><a href=\"/home\" data-ng-click=\"\">Vis hurtigtaster</a></li>\n" +
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
