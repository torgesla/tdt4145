package tdt4145;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Meny {
	static ResultSet result;
	static String query;
	public static void main(String[] args) throws SQLException {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("\n"
					+ "###########################\n"
					+ "Vil du:\n"
					+ "1) Registere apparat\n"
					+ "2) Registere øvelse\n"
					+ "3) Registrere treningsøkt\n"
					+ "4) Sjå n siste treningsøkter\n"
					+ "5) Se resultatlogg i tidsintervall\n"
					+ "6) Lage øvelsesgruppe\n"
					+ "7) Finne øvelser i samme gruppe\n"
					+ "8) Finne øvelser til apparat\n"
					+ "9) Printe en tabell\n"
					+ "0) Avslutt\n"
					+ "##############################\n");
			int svar = Integer.parseInt(scanner.nextLine());
			if(svar == 0) {
				break;
				
			}else if(svar == 1) { //Funker
				System.out.println("Hva er apparatets navn *enter* beskrivelse?");
				String navn = scanner.nextLine();
				String beskrivelse = scanner.nextLine();
				query = "INSERT INTO apparat values(\""+navn+"\",\""+beskrivelse+"\")";
				Driver.Write(query);
				
			}else if(svar == 2){ //Funker, fortsetter ikke i while-loopen, why? 
				System.out.println("NavnPåØvelse *enter* med eller uten apparat?");
				String navn =scanner.nextLine();
				String medUten = scanner.nextLine();
				query = "insert ignore into øvelse values(\""+navn+"\")";
				Driver.Write(query);
				if(medUten.equals("med")) {
					System.out.println("(kilo *komma* sett *komma* apparatnavn) adskilt av *enter*");
					while(scanner.hasNextLine()) {
						String[] input = scanner.nextLine().split(",");
						if (input.equals("quit")) {
							break;
						}
						else {
						query = "insert ignore into apparat values(\""+input[2]+"\",\" NULL \")";
						Driver.Write(query);
						query = "INSERT INTO øvelsemedapparat values( \""+ navn+"\","+Integer.parseInt(input[0])+","+Integer.parseInt(input[1])+",\""+input[2]+"\")";
						Driver.Write(query);
						}
					}
				}
				
			}else if(svar == 3) { //Funker
				System.out.println("dato(ÅÅÅÅMMDD), tidspunkt(tt:mm:ss), varighet(tt:mm:ss), personligForm, prestasjon, notat?");
				String dato = scanner.nextLine();
				String tidspunkt = scanner.nextLine();
				String varighet = scanner.nextLine();
				int form = Integer.parseInt(scanner.nextLine());
				int prestasjon = Integer.parseInt(scanner.nextLine());
				String notat = scanner.nextLine();
				query = "INSERT INTO Treningsøkt (dato,tidspunkt,varighet,personligForm,prestasjon,notat)"
						+ " values (\""+dato+"\",\""+tidspunkt+"\",\""+varighet+"\","+form+","+prestasjon+",\""+notat+"\")";	
				Driver.Write(query);
				query = "select count(*) from treningsøkt";
				result = Driver.Read(query);
				result.next();
				int øktid = result.getInt(1);
				System.out.println("Øvelser separert av komma");
				String[] øvelser = scanner.nextLine().split(",");
				for (String øvelse : øvelser) {
					query = "insert into øvelserfortreningsøkt values("+øktid+",\""+øvelse+"\")";
					Driver.Write(query);
				}
				
			}else if(svar == 4) { //Funker
				System.out.println("skriv inn en n");
				int n = Integer.parseInt(scanner.nextLine());
				query = "SELECT * FROM treningsøkt ORDER BY dato DESC,tidspunkt DESC LIMIT "+ n;
				result = Driver.Read(query);
				Driver.PrintSet(result);
			}
			else if(svar == 5) { //Funker ikke
				System.out.println("Start: tt:mm:ss,ÅÅÅÅMMDD *enter*\n"
						+ "Slutt: tt:mm:ss,ÅÅMMDD");
				String[] start = scanner.nextLine().split(",");
				String[] slutt = scanner.nextLine().split(",");
				
				query = "SELECT * \n" 
						+"FROM Øvelse NATURAL JOIN ØvelserForTreningsøkt NATURAL JOIN Treningsøkt \n"
						+ "where (treningsøkt.dato>=\""+start[1]+"\" \n"
						+ "and treningsøkt.dato<=\""+slutt[1]+"\" \n"
						+ "and treningsøkt.tidspunkt>=\""+start[0]+"\" \n"
						+ "and treningsøkt.tidspunkt<=\""+slutt[0]+"\" or treningsøkt.tidspunkt,treningsøkt.varighet>=tidsstart \n"
						+ "and treningsøkt.tidspunkt,treningsøkt.varighet<=tidslutt) or (treningsøkt.tidspunkt<=tidsstart \n"
						+ "and AddTime(treningsøkt.tidspunkt,treningsøkt.varighet)>=tidslutt)))";
				result = Driver.Read(query);
				Driver.PrintSet(result);
				
			}else if(svar == 6) { //Funker
				ArrayList<String> øvelseliste = new ArrayList<>();
				System.out.println("Hvilken muskelgruppe?");
				String muskelgruppeNavn = scanner.nextLine();
				Driver.PrintTable("øvelse");
				while(true) {
					System.out.println("Skriv inn ønsket øvelse fra listen, \"quit\" for å hoppe ut av loop");
					String nyØvelse = scanner.nextLine();
					if (nyØvelse.equals("quit")) {
						break;
					}
					else {
						øvelseliste.add(nyØvelse);
					}
				}
				query = "insert ignore into øvelsesgruppe values(\""+muskelgruppeNavn+"\")";
				Driver.Write(query);
				for (String øvelse : øvelseliste) {
					query = "insert ignore into gruppeforøvelse values(\""+øvelse+"\",\""+muskelgruppeNavn+"\")";
					Driver.Write(query);
				}
			}
			else if(svar == 7) { //Funker
				Driver.PrintTable("øvelsesgruppe");
				System.out.println("Hvilken gruppe vil du se øvelser fra?");
				String gruppe = scanner.nextLine();
				query = "select øvelsesnavn from gruppeforøvelse where gruppenavn=\""+gruppe+"\"";
				result = Driver.Read(query);
				Driver.PrintSet(result);
				
			}
			else if(svar == 8) { //Funker
				System.out.println("Skriv navn på apparat du vil se øvelser til: ");
				String apparat = scanner.nextLine();
				query = "SELECT * "
						+"FROM ØvelseMedApparat inner join apparat on øvelsemedapparat.apparatnavn = apparat.navn"
						+" where apparat.navn= \""+apparat+"\"";
				result = Driver.Read(query);
				Driver.PrintSet(result);
			}
			else if(svar == 9) { //Funker
				System.out.println("Hva vil du printe?");
				String skalPrintes = scanner.nextLine();
				Driver.PrintTable(skalPrintes);
				result = Driver.Read("select * from \""+skalPrintes+"\"");
				Driver.PrintSet(result);
			}
			else{
				System.out.println("Illegal input: try again");
			}
		}
	}
}
