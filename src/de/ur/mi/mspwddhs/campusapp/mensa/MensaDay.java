package de.ur.mi.mspwddhs.campusapp.mensa;

import java.util.ArrayList;
import de.ur.mi.mspwddhs.campusapp.database.Database;

public class MensaDay {
	private String date;
	private ArrayList<String> suppen;
	private ArrayList<String> haupt;
	private ArrayList<String> beilage;
	private ArrayList<String> nachtisch;

	public MensaDay(String date, Database db) {
		this.date = date;
		suppen = db.getProdcuts(date, "Suppe");
		haupt = db.getProdcuts(date, "HG");
		beilage = db.getProdcuts(date, "B");
		nachtisch = db.getProdcuts(date, "N");
	}

	public ArrayList<String> getSuppe() {
		return suppen;
	}

	public ArrayList<String> getHaupt() {
		return haupt;
	}

	public ArrayList<String> getBeilagen() {
		return beilage;
	}

	public ArrayList<String> getNachtisch() {
		return nachtisch;
	}

	public String getDate() {
		return date;
	}
}