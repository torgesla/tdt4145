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
					+ "2) Registere �velse\n"
					+ "3) Registrere trenings�kt\n"
					+ "4) Sj� n siste trenings�kter\n"
					+ "5) Se resultatlogg i tidsintervall\n"
					+ "6) Lage �velsesgruppe\n"
					+ "7) Finne �velser i samme gruppe\n"
					+ "8) Finne �velser til apparat\n"
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
				query = "INSERT INTO apparat values('"+navn+"','"+beskrivelse+"')";
				Driver.Write(query);
				
			}else if(svar == 2){ //Funker
				System.out.println("Navn p� �velse *enter* med eller uten apparat");
				String navnP��velse = scanner.nextLine();
				query = "insert ignore into �velse values('"+navnP��velse+"')";
				Driver.Write(query);
				String medUten =scanner.nextLine();
				if (medUten.equals("med")) {
					System.out.println("(kilo *komma* sett *komma* apparatnavn)*enter*");
					String[] input = scanner.nextLine().split(",");
					query = "insert ignore into apparat values('"+input[2]+"','Null')";
					Driver.Write(query);
					query = "INSERT INTO �velsemedapparat values( '"+ navnP��velse+"',"+Integer.parseInt(input[0])+","+Integer.parseInt(input[1])+",'"+input[2]+"')";
					Driver.Write(query);
				} else {
					System.out.println("Beskrivelse for �velse uten apparat?");
					String beskrivelse = scanner.nextLine();
					query = "insert into �velseutenapparat values('"+navnP��velse+"','"+beskrivelse+"')";
					Driver.Write(query);
				}
				
			}else if(svar == 3) { //Funker
				System.out.println("dato(����MMDD), tidspunkt(tt:mm:ss), varighet(tt:mm:ss), personligForm, prestasjon, notat?");
				String dato = scanner.nextLine();
				String tidspunkt = scanner.nextLine();
				String varighet = scanner.nextLine();
				int form = Integer.parseInt(scanner.nextLine());
				int prestasjon = Integer.parseInt(scanner.nextLine());
				String notat = scanner.nextLine();
				query = "INSERT INTO Trenings�kt (dato,tidspunkt,varighet,personligForm,prestasjon,notat)"
						+ " values (\""+dato+"\",\""+tidspunkt+"\",\""+varighet+"\","+form+","+prestasjon+",\""+notat+"\")";	
				Driver.Write(query);
				query = "select count(*) from trenings�kt";
				result = Driver.Read(query);
				result.next();
				int �ktid = result.getInt(1);
				System.out.println("�velser separert av komma");
				String[] �velser = scanner.nextLine().split(",");
				for (String �velse : �velser) {
					query = "insert into �velserfortrenings�kt values("+�ktid+",\""+�velse+"\")";
					Driver.Write(query);
				}
				
			}else if(svar == 4) { //Funker
				System.out.println("skriv inn en n");
				int n = Integer.parseInt(scanner.nextLine());
				query = "SELECT * FROM trenings�kt natural ORDER BY dato DESC,tidspunkt DESC LIMIT "+ n;
				result = Driver.Read(query);
				Driver.PrintSet(result);
			}
			else if(svar == 5) { //Funker
				System.out.println("Start: ����MMDD *enter*\n"
						+ "Slutt: ��MMDD");
				String start = scanner.nextLine();
				String slutt = scanner.nextLine();
				
				query = "select personligform,prestasjon,notat, �velserfortrenings�kt.navn,kilo,sett from trenings�kt\n"
						+"natural join �velserfortrenings�kt \n"
						+"left join �velsemedapparat on �velserfortrenings�kt.navn = �velsemedapparat.navn"
						+" where dato between '"+ start+"' and '"+slutt+"';";
				System.out.println(query);
				result = Driver.Read(query);
				Driver.PrintSet(result);
				
			}else if(svar == 6) { //Funker
				ArrayList<String> �velseliste = new ArrayList<>();
				System.out.println("Hvilken muskelgruppe?");
				String muskelgruppeNavn = scanner.nextLine();
				Driver.PrintTable("�velse");
				while(true) {
					System.out.println("Skriv inn �nsket �velse fra listen, \"quit\" for � hoppe ut av loop");
					String ny�velse = scanner.nextLine();
					if (ny�velse.equals("quit")) {
						break;
					}
					else {
						�velseliste.add(ny�velse);
					}
				}
				query = "insert ignore into �velsesgruppe values(\""+muskelgruppeNavn+"\")";
				Driver.Write(query);
				for (String �velse : �velseliste) {
					query = "insert ignore into gruppefor�velse values(\""+�velse+"\",\""+muskelgruppeNavn+"\")";
					Driver.Write(query);
				}
			}
			else if(svar == 7) { //Funker
				Driver.PrintTable("�velsesgruppe");
				System.out.println("Hvilken gruppe vil du se �velser fra?");
				String gruppe = scanner.nextLine();
				query = "select �velsesnavn from gruppefor�velse where gruppenavn=\""+gruppe+"\"";
				result = Driver.Read(query);
				Driver.PrintSet(result);
				
			}
			else if(svar == 8) { //Funker
				System.out.println("Skriv navn p� apparat du vil se �velser til: ");
				String apparat = scanner.nextLine();
				query = "SELECT * "
						+"FROM �velseMedApparat inner join apparat on �velsemedapparat.apparatnavn = apparat.navn"
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
