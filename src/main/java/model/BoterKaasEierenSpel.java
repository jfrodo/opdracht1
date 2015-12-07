package model;

import java.util.Random;

/**
 * Klasse voor het spelen van Boter Kaas en Eieren. 
 * Wie begint (KRUIS of NUL) wordt meegegeven aan de constructor.
 * De klasse heeft methoden voor het verwerken van een zet maar ook
 * voor het bedenken van een zet. Deze laatste kan gebruikt worden
 * om tegen de computer te kunnen spelen. Een bedachte zet is naar
 * keuze slim of dom.
 * <p>
 * Een slimme zet wordt bepaald in drie stappen:
 * <ul>
 * <li>als er een winnende zet is, kies die</li>
 * <li>anders als er een zet is die voorkomt dat de
 * tegenstender wint, kies die</li>
 * <li>anders kies een gunstig leeg veld (bij voorkeur het
 * centrum, dan een van de hoeken, dan de rest)
 * </li> 
 * </ul>
 * <p>
 * Deze strategie is overigens niet optimaal; hij leidt nooit
 * tot verlies maar winstkansen worden soms over het hoofd gezien.
 * <p>
 * Bij een niet-slimme zet wordt een willeurig leeg veld gekozen.
 * <p>
 * De klasse houdt de status van het spel bij en
 * garandeert een juiste afwisseling van zetten. 
 */
public class BoterKaasEierenSpel {
  
  /*
   * De indices van de acht lijnen op het veld
   */
  private static final int[][] lijnen = 
      {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
       {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
       {0, 4, 8}, {2, 4, 6}};  
  
  private Markering[] speelbord = new Markering[9]; 
  private boolean bedenkSlim = true; 
  private Markering beurt = null;
  private Status status = Status.BEZIG; 

  /**
   * Constructor zet het bord op
   * @param beurtBijStart  is de eerste zet KRUIS of NUL?
   */
  public BoterKaasEierenSpel(Markering beurtBijStart) {
    for (int i=0; i < 9; i++) {
      speelbord[i] = Markering.LEEG;
    }
    beurt = beurtBijStart;
  }
  
  // get-methoden
  
  /**
   * Geeft de markering van het veld met de gegeven index
   * @param index  de gegeven index
   * @return  de markering van het veld met die index
   */
  public Markering getMarkering(int index) {
    return speelbord[index];
  }

  /**
   * Geeft aan of de computer slim of dom speelt
   * @return  true als de computer slim speelt, anders false
   */
  public boolean isBedenkSlim() {
    return bedenkSlim;
  }
  
  /**
   * Geeft de huidige status van het spel
   * @return  de huidige status van het spel
   */
  public Status getStatus() {
    return status;
  }
   
  // set-methoden
  
  /**
   * Wijzigt de speelwijze van de computer
   * @param bedenkSlim  true voor slim; false voor dom
   */
  public void setBedenkSlim(boolean bedenkSlim) {
    this.bedenkSlim = bedenkSlim;
  }
  
  /**
   * Verwerkt een zet met een gegeven markering; 
   * als die markering niet aan de beurt is
   * of als het spel al is afgelopen, doet de methode niets. 
   * @param markering  kruis of nul (afhankelijk van wie er zet)
   * @param index  de index van het veld waarop gezet wordt
   */
  public void verwerkZet(Markering markering, int index) {
    if (beurt.equals(markering) && 
		status.equals(Status.BEZIG) &&
		speelbord[index].equals(Markering.LEEG)) {  
      speelbord[index] = markering;
      werkStatusBij();
      beurt = andereMarkering(beurt);
    }
  }
  

  /**
   * Bedenk een zet voor de gegeven markering.
   * @return de index van de bedachte zet
   */
  public int bedenkZet(Markering markering) {
    if (bedenkSlim) {
      return slimmeZet(markering);
    }
    else {
      return randomZet();
    }
  }

  // private methoden
  
  private Markering andereMarkering(Markering markering) {
    if (markering.equals(Markering.KRUIS)) {
      return Markering.NUL;
    }
    else if (markering.equals(Markering.NUL)) {
      return Markering.KRUIS;
    }
    else { // dit komt niet voor
      return Markering.LEEG;
    }
  }
  
  /**
   * Werkt de status van het spel bij:
   * - als er een lijn is met drie kruizen, wint KRUIS
   * - als er een lijn is met drie nullen, wint NUL
   * - als er geen leeg veld meer is, is het gelijk
   * - anders is het spel nog bezig.
   * Als het spel al was afgelopen, doet de methode niets
   */
  private void werkStatusBij() {
    if (status.equals(Status.BEZIG)) {
      boolean nogLeegVeld = false;
      for (int[] lijn: lijnen) {
        if (aantalOpLijn(lijn, Markering.KRUIS) == 3) {
          status = Status.KRUISWINT;   
          return;
        }
        else if (aantalOpLijn(lijn, Markering.NUL) == 3) {
          status = Status.NULWINT;
          return;
        } 
        else if (eersteLegePlek(lijn) != -1) {
          // hier mag geen return staan, want er
          // kan winst zijn op een andere lijn!
          nogLeegVeld = true;
        }
      } 
      if (!nogLeegVeld) {
        status = Status.GELIJK;
      }
    }
  }

  /**
   * Bedenkt een slimme zet
   * @return de index van een geschikt leeg veld 
   */
  private int slimmeZet(Markering markering) {
    int gekozenIndex;
    gekozenIndex = winnendeOfGedwongenZet(markering);
    if (gekozenIndex == -1) {
      gekozenIndex = winnendeOfGedwongenZet(andereMarkering(markering));
      if (gekozenIndex == -1) {
        gekozenIndex = geschikteZet();
      }
    }
    return gekozenIndex;
  }
  
  /**
   * Geeft de index terug van een willekeurig leeg veld.
   * Het zoeken begint op een random bepaalde positie.
   * Preconditie: er is nog een leeg veld
   * @return de index van een leeg veld
   */
  private int randomZet() {
    Random random = new Random();
    int index = random.nextInt(9);
    for (int n = 0; n < 9; n++) {
      Markering markering = speelbord[index];
      if (markering.equals(Markering.LEEG)) {
        return index;
      }
      else {
        index = (index + 1) % 9;
      }
    }
    // onbereikbaar want er is altijd een veld leeg
    return -1; 
  }  
  
  /**
   * Bepaalt of er een lijn is met twee kruizen of
   * twee nullen. Als dat zo is, dan wordt de index
   * van de derde, lege positie op de lijn teruggegeven.
   * @param markering  kruis of nul, afhankelijk van wat gezocht wordt
   * @return  de derde positie op de lijn of -1 als er niet zo'n lijn is
   */
  private int winnendeOfGedwongenZet(Markering markering) {
    for (int[] lijn: lijnen) {
      int n = aantalOpLijn(lijn, markering);
      if (n == 2) {
        int leeg = eersteLegePlek(lijn);
        if (leeg != -1) {
          return leeg;
        }
      }
    }
    return -1;
  }
  
  /**
   * Zoekt een geschikt leeg veld als er geen winnende of
   * gedwongen zet is. Het midden heeft de eerste voorkeur,
   * dan één van de vier hoeken, dan de andere velden.
   * Preconditie: er is nog een leeg veld
   * @return  de index van een geschikt vrije veld
   */
  private int geschikteZet() {
    int [] volgorde = {4, 0, 2, 6, 8, 1, 3, 5, 7};
    for (int index: volgorde) {
      if (speelbord[index].equals(Markering.LEEG)) {
        return index;
      }
    }
    // onbereikbaar want er is altijd een veld leeg
    return -1;
  }
  

  
  /**
   * Bepaalt het aantal kruizen of nullen op een
   * gegeven lijn
   * @param lijn  de lijn die wordt onderzocht
   * @param markering  de markering die wordt onderzocht
   * @return  het aantal kruizen of nullen op de lijn
   */
  private int aantalOpLijn(int[] lijn, Markering markering){
    int n = 0;
    for (int index: lijn) {
      if (speelbord[index].equals(markering)) {
        n++;
      }
    }
    return n;
  }
  
  /**
   * Bepaalt de eerste lege plek op een gegeven lijn
   * @param lijn  de onderzochte lijn
   * @return  de index van de eerste lege plek of -1 als er
   *          geen lege plek is.
   */
  private int eersteLegePlek(int[] lijn) {
    for (int index: lijn) {
      if (speelbord[index].equals(Markering.LEEG)) {
        return index;
      }
    }
    return -1;
  }
    
}
