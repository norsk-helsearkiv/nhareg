angular.module('templates-app', ['common/http-service/error-modal-400.tpl.html', 'common/http-service/error-modal-404.tpl.html', 'common/http-service/error-modal-409.tpl.html', 'common/http-service/error-modal-500.tpl.html', 'common/list-view/list-view.tpl.html', 'common/modal-service/delete-modal.tpl.html', 'common/modal-service/ny-avlevering.tpl.html', 'common/modal-service/ny-avtale.tpl.html', 'home/home.tpl.html', 'login/login.tpl.html', 'registrering/registrering.tpl.html']);

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

angular.module("common/http-service/error-modal-409.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("common/http-service/error-modal-409.tpl.html",
    "<div class=\"modal-header\">\n" +
    "    <h3 class=\"modal-title\">{{'error.RESOURCE_CONFLICT_CODE' | translate}}</h3>\n" +
    "</div>\n" +
    "\n" +
    "<div class=\"modal-body\">\n" +
    "    <p>\n" +
    "        {{'error.RESOURCE_CONFLICT' | translate}}\n" +
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
    "            <table class=\"table table-striped table-hover\">\n" +
    "                <thead>\n" +
    "                    <tr>\n" +
    "                        <th>{{ 'home.AVTALE' | translate }}</th>\n" +
    "                        <th>{{ 'home.AVLEVERING' | translate }}</th>\n" +
    "                        <th>{{ 'common.table.ID' | translate }}</th>\n" +
    "                        <th>{{ 'common.table.NAVN' | translate }}</th>\n" +
    "                        <th></th>\n" +
    "                    </tr>\n" +
    "                </thead>\n" +
    "                <tbody>\n" +
    "                    <tr class=\"clickable\" data-ng-repeat=\"pasient in data.liste\">\n" +
    "                        <td data-ng-click=\"actionVisJournal(pasient.uuid)\">{{ pasient.avtale }}</td>\n" +
    "                        <td data-ng-click=\"actionVisJournal(pasient.uuid)\">{{ pasient.avlevering }}</td>\n" +
    "                        <td data-ng-click=\"actionVisJournal(pasient.uuid)\">{{ pasient.fodselsnummer }}</td>\n" +
    "                        <td data-ng-click=\"actionVisJournal(pasient.uuid)\">{{ pasient.navn }}</td>\n" +
    "                        <td>\n" +
    "                            <button class=\"icon icon-padding-left icon-delete\" data-ng-click=\"actionFjernPasientjournal(pasient)\" tooltip=\"{{text.tooltip.deleteElement}}\"></button>\n" +
    "                        </td>\n" +
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
    "			<label>{{ 'common.table.ID' | translate }}</label>\n" +
    "			<input type=\"text\" class=\"form-control\" ng-model=\"formData.avleveringsidentifikator\" data-ng-show=\"!erEndring\">\n" +
    "			<p data-ng-show=\"erEndring\">{{formData.avleveringsidentifikator}}</p>\n" +
    "			<label class=\"label-error\" data-ng-show=\"formData.error.avleveringsidentifikator\">{{formData.error.avleveringsidentifikator}}</label>\n" +
    "		</div>\n" +
    "		<div class=\"form-group\">\n" +
    "			<label>{{ 'common.table.ARKIVSKAPER' | translate }}</label>\n" +
    "			<input type=\"text\" class=\"form-control\" ng-model=\"formData.arkivskaper\">\n" +
    "			<label class=\"label-error\" data-ng-show=\"formData.error.arkivskaper\">{{formData.error.arkivskaper}}</label>\n" +
    "		</div>\n" +
    "		<div class=\"form-group\">\n" +
    "			<label>{{ 'common.table.BESKRIVELSE' | translate }}</label>\n" +
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
    "			<label>{{ 'common.table.ID' | translate }}</label>\n" +
    "			<input type=\"text\" class=\"form-control\" ng-model=\"formData.avtaleidentifikator\" data-ng-show=\"!erEndring\">\n" +
    "			<p data-ng-show=\"erEndring\">{{formData.avtaleidentifikator}}</p>\n" +
    "			<label class=\"label-error\" data-ng-show=\"formData.error.avtaleidentifikator\">{{formData.error.avtaleidentifikator}}</label>\n" +
    "		</div>\n" +
    "		<div class=\"form-group\">\n" +
    "			<label>{{ 'common.table.BESKRIVELSE' | translate }}</label>\n" +
    "			<input type=\"text\" class=\"form-control\" ng-model=\"formData.avtalebeskrivelse\">\n" +
    "			<label class=\"label-error\" data-ng-show=\"formData.error.avtalebeskrivelse\">{{formData.error.avtalebeskrivelse}}</label>\n" +
    "		</div>\n" +
    "		<div class=\"form-group\">\n" +
    "			<label>{{ 'common.table.DATO' | translate }}</label>\n" +
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
    "<div class=\"container-fluid\">\n" +
    "  <div class=\"row\">\n" +
    "    <!-- Avtaleliste -->\n" +
    "    <div class=\"col-sm-3 col-md-2 sidebar\">\n" +
    "      <h4>\n" +
    "        {{ 'home.AVTALER' | translate }}\n" +
    "        <button class=\"right btn btn-small btn-success\" data-ng-click=\"actionLeggTilAvtale()\">{{ 'common.LEGG_TIL' | translate }}</button>\n" +
    "      </h4>\n" +
    "      <ul class=\"nav nav-sidebar\">\n" +
    "        <li data-ng-repeat=\"avtale in avtaler\"\n" +
    "            data-ng-click=\"setValgtAvtale(avtale)\"\n" +
    "            data-ng-class=\"{ active: valgtAvtale.avtaleidentifikator === avtale.avtaleidentifikator}\">\n" +
    "            <a href=\"\">\n" +
    "              <button class=\"icon icon-edit\" data-ng-click=\"actionEndreAvtale(avtale)\" tooltip=\"{{text.tooltip.endr}}\"></button>\n" +
    "              <button class=\"icon icon-delete icon-padding-right\" data-ng-click=\"actionDeleteAvtale(text.avtale, avtale.avtaleidentifikator, avtale)\" tooltip=\"{{text.tooltip.deleteElement}}\" data-ng-show=\"valgtAvtale.avtaleidentifikator === avtale.avtaleidentifikator && avleveringer.length === 0\">\n" +
    "              </button>\n" +
    "              {{ avtale.avtalebeskrivelse }}\n" +
    "            </a>\n" +
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
    "      <h2 class=\"sub-header\">\n" +
    "        {{ 'home.AVLEVERINGER' | translate }}\n" +
    "        <small> {{ 'home.FOR' | translate }} {{ valgtAvtale.avtalebeskrivelse }}</small>\n" +
    "        <button class=\"right btn btn-success\" data-ng-click=\"actionLeggTilAvlevering()\" data-ng-show=\"valgtAvtale\">\n" +
    "          {{ 'common.LEGG_TIL' | translate }}\n" +
    "        </button>\n" +
    "      </h2>\n" +
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
    "                <button class=\"icon icon-padding-left icon-add-journal\" data-ng-click=\"actionLeggTilPasientjournald(avlevering)\" tooltip=\"{{text.tooltip.add}}\"></button>\n" +
    "                <button class=\"icon icon-padding-left icon-folder\" data-ng-click=\"actionAvleveringLeveranse(avlevering)\" tooltip=\"{{text.tooltip.folder}}\"></button>\n" +
    "                <button class=\"icon icon-padding-left icon-edit\" data-ng-click=\"actionEndreAvlevering(avlevering)\" tooltip=\"{{text.tooltip.endre}}\"></button>\n" +
    "                <button class=\"icon icon-padding-left icon-delete\" data-ng-click=\"actionFjernAvlevering(text.avlevering, avlevering.avleveringsidentifikator, avlevering)\" tooltip=\"{{text.tooltip.deleteElement}}\" data-ng-show=\"avlevering.pasientjournal.length === 0\"></button>\n" +
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
    "      	<!--<li><a href=\"\" data-ng-click=\"navHome()\">{{ 'common.VIS_HURTIGTASTER' | translate }}</a></li>-->\n" +
    "        <li><a class=\"loggut\" href=\"\" data-ng-click=\"loggUt()\">{{ 'login.LOGG_UT' | translate }}</a></li>\n" +
    "      </ul>\n" +
    "  </div>\n" +
    "</nav>\n" +
    "\n" +
    "<div class=\"content\">\n" +
    "	<div class=\"col-sm-3 col-md-2 sidebar\">\n" +
    "		<h4>Valideringsfeil</h4>\n" +
    "		<ul class=\"nav nav-sidebar\">\n" +
    "			<li data-ng-repeat=\"feil in feilmeldinger\">\n" +
    "				<span class=\"error error-tittel\">{{feil.felt}}</span><br />\n" +
    "				<span class=\"error-beskrivelse\">{{feil.feilmelding}}</span>\n" +
    "			</li>\n" +
    "		</ul>\n" +
    "    </div>\n" +
    "    <div class=\"col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main\">\n" +
    "		<h1>\n" +
    "			Registrering\n" +
    "			<small>{{avlevering.avleveringsbeskrivelse}}</small>\n" +
    "		</h1>\n" +
    "		<form>\n" +
    "			<div class=\"well\">\n" +
    "				<label id=\"labelLagringsenhet\" data-ng-class=\"{error: error['lagringsenheter'] !== undefined }\">\n" +
    "					Lagringsenheter \n" +
    "				</label><small> (Klikk 'enter' for å legge til flere)</small>\n" +
    "				<input type=\"text\" id=\"lagringsenhet\" class=\"form-control\" placeholder=\"Lagringsenhet...\" data-ng-model=\"lagringsenhet\" ng-enter=\"keyAddLagringsenhet()\" autofocus>\n" +
    "				<ul id=\"lagringsenhet-liste\">\n" +
    "					<li data-ng-repeat=\"enhet in formData.lagringsenheter\">\n" +
    "						<button type=\"button\" class=\"btn\" data-ng-click=\"actionFjernLagringsenhet(enhet)\"></button>\n" +
    "						{{ enhet }}\n" +
    "					</li>\n" +
    "				</ul>\n" +
    "				<div class=\"row\"><!-- Brukes for å sørge for at well utvider seg når float:left listen går over flere linjer --></div>\n" +
    "			</div>\n" +
    "\n" +
    "			<div class=\"well\">\n" +
    "				<div class=\"row\">\n" +
    "					<div class=\"span span3\">\n" +
    "						<label id=\"journalnummer\" data-ng-class=\"{error: error['journalnummer'] !== undefined }\">Journalnummer</label>\n" +
    "						<input type=\"text\" id=\"journalnummerInput\" class=\"form-control\" placeholder=\"Journalnummer...\" data-ng-model=\"formData.journalnummer\">\n" +
    "					</div>\n" +
    "					<div class=\"span span3\">\n" +
    "						<label id=\"lopenummer\" data-ng-class=\"{error: error['lopenummer'] !== undefined }\">Løpenummer</label>\n" +
    "						<input type=\"text\" class=\"form-control\" placeholder=\"Løpenummer...\" data-ng-model=\"formData.lopenummer\">\n" +
    "					</div>\n" +
    "					<div class=\"span span3\">\n" +
    "						<label id=\"fodselsnummer\" data-ng-class=\"{error: error['fodselsnummer'] !== undefined }\">Fødselsnummer</label>\n" +
    "						<input type=\"text\" class=\"form-control\" placeholder=\"Fødselsnummer...\" data-ng-model=\"formData.fodselsnummer\" data-ng-focus=\"setFnr()\" data-ng-blur=\"populerFelt()\">\n" +
    "					</div>\n" +
    "				</div>\n" +
    "			</div>\n" +
    "\n" +
    "			<div class=\"well\">\n" +
    "				<div class=\"row\">\n" +
    "					<div class=\"span span43 padding-bottom\">\n" +
    "						<label id=\"navn\" data-ng-class=\"{error: error['navn'] !== undefined }\">{{ 'registrer.NAVN' | translate }}</label>\n" +
    "						<input type=\"text\" class=\"form-control\" placeholder=\"Navn...\" data-ng-model=\"formData.navn\" data-ng-blur=\"setFocusEtterNavn()\">\n" +
    "					</div>\n" +
    "					<div class=\"span span4\">\n" +
    "						<label id=\"kjonn\" data-ng-class=\"{error: error['kjonn'] !== undefined }\">Kjønn</label>\n" +
    "					    <select  class=\"form-control\" data-ng-model=\"formData.kjonn\"\n" +
    "	            			ng-options=\"item as item.tekst for item in kjonn\">\n" +
    "	            			<option value=\"\" disabled selected>Kjønn...</option>\n" +
    "	            	</select>\n" +
    "					</div>\n" +
    "				</div>\n" +
    "\n" +
    "				<div class=\"row\">\n" +
    "					<div class=\"span span4\">\n" +
    "						<label id=\"fodt\" data-ng-class=\"{error: error['fodt'] !== undefined }\">Født</label>\n" +
    "						<input type=\"text\" class=\"form-control\" placeholder=\"dd.mm.åååå\" data-ng-model=\"formData.fodt\">\n" +
    "					</div>\n" +
    "					<div class=\"span span4\">\n" +
    "						<label id=\"dod\"  data-ng-class=\"{error: error['dod'] !== undefined }\">Død</label>\n" +
    "						<input type=\"text\" class=\"form-control\" placeholder=\"dd.mm.åååå\" data-ng-model=\"formData.dod\" id=\"ddato\">\n" +
    "					</div>\n" +
    "					<div class=\"span span4\">\n" +
    "						<label id=\"fKontakt\"  data-ng-class=\"{error: error['fKontakt'] !== undefined }\">Første kontakt</label>\n" +
    "						<input type=\"text\" class=\"form-control\" placeholder=\"dd.mm.åååå\" data-ng-model=\"formData.fKontakt\">\n" +
    "					</div>\n" +
    "					<div class=\"span span4\">\n" +
    "						<label id=\"sKontakt\"  data-ng-class=\"{error: error['sKontakt'] !== undefined }\">Siste kontakt</label>\n" +
    "						<input type=\"text\" class=\"form-control\" placeholder=\"dd.mm.åååå\" data-ng-model=\"formData.sKontakt\">\n" +
    "					</div>\n" +
    "				</div>\n" +
    "			</div>\n" +
    "\n" +
    "			<div class=\"well\">\n" +
    "				<label>Diagnoser</label>\n" +
    "			</div>\n" +
    "\n" +
    "			<div class=\"well\">\n" +
    "				<label>Vedlegg</label>\n" +
    "			</div>\n" +
    "\n" +
    "			<button type=\"submit\" class=\"btn btn-primary right\" ng-btnFocus=\"\" data-ng-click=\"submit()\">Kontroller og lagre</button>\n" +
    "			<button class=\"btn btn-default\" ng-btn-focus>Tilbake</button>\n" +
    "		</form>\n" +
    "	</div>\n" +
    "</div>");
}]);
