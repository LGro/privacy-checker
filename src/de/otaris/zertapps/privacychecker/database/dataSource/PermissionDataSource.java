package de.otaris.zertapps.privacychecker.database.dataSource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import de.otaris.zertapps.privacychecker.database.DatabaseHelper;
import de.otaris.zertapps.privacychecker.database.model.Permission;

/**
 * Handles requests concerning Permissions to the database.
 * 
 */
public class PermissionDataSource extends DataSource<Permission> {

	private String[] allColumns = { Permission.ID, Permission.NAME,
			Permission.LABEL, Permission.DESCRIPTION, Permission.CRITICALITY };

	public PermissionDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	/**
	 * convert database result cursor (pointing to a result set) to app object
	 * 
	 * @param cursor
	 * @return cursor data as App object
	 */
	protected Permission cursorToModel(Cursor cursor) {
		
		if(cursor.getCount() == 0)
			return null;	
		
		Permission permission = new Permission();

		permission.setId(cursor.getInt(0));
		permission.setName(cursor.getString(1));
		permission.setLabel(cursor.getString(2));
		permission.setDescription(cursor.getString(3));
		permission.setCriticality(cursor.getInt(4));

		return permission;
	}

	/**
	 * create app in DB by all attributes
	 * 
	 * @param name
	 * @param label
	 * @param version
	 * @param installed
	 * @param rating
	 * @return app object of the newly created app
	 */
	public Permission createPermission(String name, String label,
			String description, int criticality) {
		// set values for columns
		ContentValues values = new ContentValues();
		values.put(Permission.NAME, name);
		values.put(Permission.LABEL, label);
		values.put(Permission.DESCRIPTION, description);
		values.put(Permission.CRITICALITY, criticality);

		// insert into DB
		long insertId = database.insert(Permission.TABLE, null, values);

		// get recently inserted Permission by ID
		return getPermissionById(insertId);
	}
	
	public Permission createPermissionByName(String name) {
		// set values for columns
		ContentValues values = new ContentValues();
		values = setContentValuesByName(name);

		// insert into DB
		long insertId = database.insert(Permission.TABLE, null, values);
		
		// get recently inserted Permission by ID
		return getPermissionById(insertId);
	}

	/**
	 * gets single app by id from database
	 * 
	 * @param appId
	 *            id to identify a single app
	 * @return app object for given id
	 */
	public Permission getPermissionById(long permissionId) {
		// build database query
		Cursor cursor = database.query(Permission.TABLE, allColumns,
				Permission.ID + " = " + permissionId, null, null, null, null);
		cursor.moveToFirst();

		// convert to Permission object
		Permission newPermission = cursorToModel(cursor);
		cursor.close();

		// return Permission object
		return newPermission;
	}

	public Permission getPermissionByName(String permissionWithoutClassName) {
		Cursor cursor = database.query(Permission.TABLE, allColumns,
				Permission.NAME + " = '" + permissionWithoutClassName + "'", null, null, null, null);
		cursor.moveToFirst();

		Permission permission = cursorToModel(cursor);
		cursor.close();
		return permission;
	}
	
	public ContentValues setContentValuesByName(String name) {
		ContentValues values = new ContentValues();
		String label;
		String description;
		int criticality;
		
		/*
		 * The following switch is ordered by criticality. The first permission 
		 * is the most critical permission, hence it receives the max number
		 * of different permissions.
		 * 
		 * There are 146 different permissions.
		 */
		//int n = 146;
		
		switch (name) {
		case "SEND_SMS":
			label = "SMS senden";
			description = "App kann SMS versenden (ggf. im Hintergrund). Dies kann Kosten verursachen.";
			criticality = 1;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "RECEIVE_SMS":
			label = "SMS empfangen";
			description = "App kann SMS empfangen und verarbeiten. App kann an Gerät gesendete SMS überwachen und löschen (im Hintergrund).";
			criticality = 2;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "RECEIVE_MMS":
			label = "MMS empfangen";
			description = "App kann MMS empfangen und verarbeiten. App kann an Gerät gesendete MMS überwachen und löschen (im Hintergrund).";
			criticality = 3;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "READ_CELL_BROADCAST":
			label = "Cell Broadcast-Nachrichten lesen";
			description = "App kann empfangene Cell Broadcast-Nachrichten lesen.";
			criticality = 4;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "READ_SMS":
			label = "SMS oder MMS lesen";
			description = "App kann auf dem Telefon oder der SIM-Karte gespeicherte SMS bzw. MMS lesen.";
			criticality = 5;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "WRITE_SMS":
			label = "SMS oder MMS bearbeiten";
			description = "App kann auf dem Telefon oder der SIM-Karte gespeicherte SMS bzw. MMS bearbeiten. App kann auch Nachrichten löschen.";
			criticality = 6;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "RECEIVE_WAP_PUSH":
			label = "Textnachrichten (WAP) empfangen";
			description = "App kann WAP-Nachrichten empfangen und verarbeiten. Nachrichten können überwacht und gelöscht werden (im Hintergrund).";
			criticality = 7;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "READ_CONTACTS":
			label = "Kontakte lesen";
			description = "App kann gespeicherte Kontakte lesen (einschließlich der Häufigkeit, wie oft mit einer Person kommuniziert wurde). App kann diese Daten speichern und ggf. im Hintergrund weiterleiten.";
			criticality = 8;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "WRITE_CONTACTS":
			label = "Kontakte ändern";
			description = "App kann gespeicherte Kontaktdaten ändern. App kann gespeicherte Kontaktdaten löschen.";
			criticality = 9;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "READ_CALL_LOG":
			label = "Anrufliste lesen";
			description = "App kann Anrufliste lesen (einschließlich Daten über ein-/ausgehende Anrufe). App kann diese Daten speichern und ggf. im Hintergrund weiterleiten.";
			criticality = 10;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "WRITE_CALL_LOG":
			label = "Anrufliste bearbeiten";
			description = "App kann Anrufliste ändern. App kann Anrufliste löschen.";
			criticality = 11;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "READ_SOCIAL_STREAM":
			label = "In sozialem Stream lesen";
			description = "App kann auf Updates aus sozialen Netzwerken zugreifen und diese synchronisieren.";
			criticality = 12;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "WRITE_SOCIAL_STREAM":
			label = "In sozialem Stream schreiben";
			description = "App kann Updates aus sozialen Netzwerken von Ihren Freunden einblenden. App kann Nachrichten erstellen, die so erscheinen als stammten sie von einem Freund.";
			criticality = 13;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "READ_PROFILE":
			label = "Meine Kontaktkarte lesen";
			description = "App kann auf dem Gerät gespeicherte personenbezogene Profildaten (des Besitzers) lesen, einschließlich Ihres Namens und Ihrer Kontaktdaten. App kann Sie somit identifizieren und Ihre Profildaten anandere senden.";
			criticality = 14;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "WRITE_PROFILE":
			label = "Meine Kontaktkarte ändern";
			description = "App kann auf dem Gerät gespeicherte personenbezogene Profildaten (des Besitzers) ändern, einschließlich Ihres Namens und Ihrer Kontaktdaten, sowie Daten hinzufügen. App kann Sie somit identifizieren und Ihre Profildaten an andere senden.";
			criticality = 15;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "READ_CALENDAR":
			label = "Kalendertermine lesen";
			description = "App kann auf dem Gerät gespeicherte Termine lesen. App kann möglicherweise Kalenderdaten weiterleiten oder speichern.";
			criticality = 16;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "WRITE_CALENDAR":
			label = "Kalendertermine hinzufügen oder ändern";
			description = "App kann auf dem Gerät gespeicherte Termine ändern, hinzufügen oder entfernen (im Hintergrund). App kann E-Mails an Termingäste senden.";
			criticality = 17;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "READ_USER_DICTIONARY":
			label = "Begriffe lesen, die zum Wörterbuch hinzugefügt wurden";
			description = "App kann alle Wörter, Namen und Ausdrücke lesen, die ein Nutzer in seinem Wörterbuch gespeichert hat.";
			criticality = 18;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "READ_HISTORY_BOOKMARKS":
			label = "Lesezeichen für Webseiten und das Webprotokoll lesen";
			description = "App kann Verlauf aller mit dem Browser besuchten Adressen und sämtliche Lesezeichen des Browsers lesen.";
			criticality = 19;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "WRITE_HISTORY_BOOKMARKS":
			label = "Lesezeichen für Webseiten setzen und das Webprotokoll aufzeichnen";
			description = "App kann Browserverlauf und Lesezeichen ändern und löschen.";
			criticality = 20;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "ADD_VOICEMAIL":
			label = "Mailbox-Nachrichten hinzufügen";
			description = "App kann Sprachnachrichten zum Mailbox-Posteingang hinzufügen.";
			criticality = 21;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "ACCESS_FINE_LOCATION":
			label = "Genauer Standort (GPS- und netzwerkbasiert)";
			description = "App kann genaue Position anhand von GPS-Daten oder über Sendemasten/WLAN ermitteln.";
			criticality = 22;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "ACCESS_COARSE_LOCATION":
			label = "Ungefährer Standort (netzwerkbasiert)";
			description = "App kann ungefähren Standort über Sendemasten/WLAN ermitteln.";
			criticality = 23;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "ACCESS_MOCK_LOCATION":
			label = "Simulierte Standortquellen für Testzwecke";
			description = "App kann simulierte Standortquellen für Testzwecke erstellen oder einen neuen Standortanbieter installieren. App kann GPS-Standort/-Status überschreiben.";
			criticality = 24;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		case "INTERNET":
			label = "Voller Netzwerkzugriff";
			description = "App kann eigene Internetverbindungen über benutzerdefinierte Netzwerkprotokolle herstellen. Der Browser und andere Apps bieten die Möglichkeit, Daten über das Internet zu versenden. Daher ist diese Berechtigung nicht zwingend erforderlich, um Daten über das Internet versenden zu können.";
			criticality = 25;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "CHANGE_WIFI_STATE":
			label = "WLAN-Verbindungen herstellen und trennen";
			description = "App kann Verbindung zu WLAN-Zugriffpunkten herstellen und trennen, sowie Änderungen an den WLAN-Einstellungen vornehmen.";
			criticality = 26;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "CHANGE_WIMAX_STATE":
			label = "WiMAX-Status ändern";
			description = "App kann Verbindung zu WiMAX*-Netzwerken herstellen und trennen. WiMAX ist eine drahtlose Zugriffstechnik zu Breitbandinternet.";
			criticality = 27;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "BLUETOOTH":
			label = "Pairing mit Bluetooth-Geräten durchführen";
			description = "App kann die Bluetooth-Konfiguration des Gerätes einsehen und Verbindungen mit gekoppelten Geräten herstellen und akzeptieren.";
			criticality = 28;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "BLUETOOTH_ADMIN":
			label = "Auf Bluetooth-Einstellungen zugreifen";
			description = "App kann Bluetooth-Einstellungen konfigurieren, Remote-Geräte erkennen und Verbindungen zu diesen herstellen.";
			criticality = 29;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "NFC":
			label = "Nahfeldkommunikation (NFC) steuern";
			description = "App kann mit NFC-Tags, Karten und Readern kommunizieren.";
			criticality = 30;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "AUTHENTICATE_ACCOUNTS":
			label = "Konten erstellen und Passwörter festlegen";
			description = "App kann die Kontoauthentifizierungsfunktion des Konto-Managers verwenden, einschließlich Erstellen von Konten und Abrufen und Festlegen der entsprechenden Passwörter. Bspw. Google-, Twitter- oder Facebook-Konto.";
			criticality = 31;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "USE_CREDENTIALS":
			label = "Konten auf dem Gerät verwenden";
			description = "App kann sich über die Konten auf dem Gerät anmelden (Authentifizierungstoken anfordern). App kann sich somit bspw. mit dem Google-Konto anmelden. Bspw. Google-, Twitter oder Facebook-Konto.";
			criticality = 32;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "MANAGE_ACCOUNTS":
			label = "Konten hinzufügen oder entfernen";
			description = "App kann Konten dem Konto-Manager des Geräts hinzufügen oder Konten (inkl. deren Passwörter) vom Gerät löschen. Bspw. Google-, Twitter oder Facebook-Konto.";
			criticality = 33;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "CHANGE_WIFI_MULTICAST_STATE":
			label = "WLAN-Multicast-Empfang zulassen";
			description = "App kann Datenpakete empfangen, die mithilfe von Multicast-Adressen an sämtliche Geräte in einem WLAN-Netzwerk versendet wurden, nicht nur an Ihr Gerät.";
			criticality = 34;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "RECORD_AUDIO":
			label = "Audio aufnehmen";
			description = "App kann Ton mithilfe des Mikrofons aufnehmen (jederzeit, auch im Hintergrund).";
			criticality = 35;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "CAMERA":
			label = "Bilder und Videos aufnehmen";
			description = "App kann Bilder und Videos mit der Kamera aufnehmen (jederzeit und ohne Ihre Bestätigung).";
			criticality = 36;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "PROCESS_OUTGOING_CALLS":
			label = "Ausgehende Anrufe umleiten";
			description = "App kann ausgehende Anrufe verarbeiten und die zu wählende Nummer ändern. App kann ausgehende Anrufe überwachen, umleiten und unterbinden.";
			criticality = 37;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "READ_PHONE_STATE":
			label = "Telefonstatus und Identität abrufen";
			description = "App kann Telefonnummer und Geräte-IDs erfassen. App kann feststellen, ob gerade ein Gespräch geführt wird und die Rufnummer verbundener Anrufer lesen.";
			criticality = 38;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "CALL_PHONE":
			label = "Telefonnummern direkt anrufen";
			description = "App kann ohne Ihr Eingreifen Telefonnummern wählen (kein Notruf). Dies kann zu unerwarteten Kosten und Anrufen führen.";
			criticality = 39;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "USE_SIP":
			label = "Internetanrufe tätigen/annehmen";
			description = "App kann SIP-Dienste zum Tätigen und Annehmen von Internetanrufen verwenden.";
			criticality = 40;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "WRITE_EXTERNAL_STORAGE":
			label = "SD-Karteninhalte ändern oder löschen";
			description = "App kann auf die SD-Karte schreiben und Daten darauf löschen.";
			criticality = 41;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "DISABLE_KEYGUARD":
			label = "Display-Sperre deaktivieren";
			description = "App kann Tastensperre (inkl. Passwortschutz) deaktivieren. Das Telefon deaktiviert die Sperre bspw. wenn ein Anruf eingeht und aktiviert sie nach dem Gespräch wieder.";
			criticality = 42;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "GET_TASKS":
			label = "Aktive Apps abrufen";
			description = "App kann Infos zu aktuellen und kürzlich ausgeführten Aufgaben abrufen. App kann damit möglicherweise ermitteln, welche Apps auf dem Gerät zum Einsatz kommen.";
			criticality = 43;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "SYSTEM_ALERT_WINDOW":
			label = "Über andere Apps einblenden";
			description = "App kann über andere Apps oder Teile der Benutzeroberfläche Einblendungen vornehmen. Dies kann zu unerwarteten veränderten Darstellungen anderer Apps führen.";
			criticality = 44;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "INSTALL_SHORTCUT":
			label = "Verknüpfungen installieren";
			description = "App kann Verknüpfungen zum Startbildschirm hinzufügen (ohne Eingreifen des Nutzers).";
			criticality = 45;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "UNINSTALL_SHORTCUT":
			label = "Verknüpfungen deinstallieren";
			description = "App kann Verknüpfungen vom Startbildschirm entfernen (ohne Eingreifen des Nutzers).";
			criticality = 46;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "SUBSCRIBED_FEEDS_WRITE":
			label = "Abonnierte Feeds schreiben";
			description = "App kann Änderungen an kürzlich synchronisierten Newsfeeds vornehmen. App kann somit Ihre synchronisierten Newsfeeds ändern. Newsfeeds sind über das Internet angebotene Nachrichtenströme, die mit einem Feedreader gelesen werden können. Damit lassen sich Webseiten, deren Inhalt sich häufig ändert, verfolgen, ohne die Seite unmittelbar besuchen zu müssen.";
			criticality = 47;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;

		case "CLEAR_APP_CACHE":
			label = "Alle Cache-Daten der App löschen";
			description = "App kann Speicherplatz durch Löschen von Dateien in den Cache-Verzeichnissen anderer Apps freisetzen. Cache bezeichnet einen schnellen Zwischenspeicher, der (erneute) Zugriffe auf ein langsames Hintergrundmedium oder aufwändige Neuberechnungen zu vermeiden hilft.";
			criticality = 48;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
			
		default:
			label = "Selbsterstellte Berechtigung: " + name;
			description = "App fordert eine Berechtigung, die vom Entwickler selbst erstellt wurde. Die Funktionsweise ist unbekannt.";
			criticality = 1337;
			
			values.put(Permission.NAME, name);
			values.put(Permission.LABEL, label);
			values.put(Permission.DESCRIPTION, description);
			values.put(Permission.CRITICALITY, criticality);
			return values;
		}
	}
}
