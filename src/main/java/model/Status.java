package model;

/**
 * Enumeratietype voor de status van het spel Boter Kaas en Eiseren
 */
public enum Status {
  /**
   * Status die aangeeft dat het spel nog bezig is
   */
  BEZIG, 
  
  /**
   * Status die aangeeft dat KRUIS heeft gewonnen
   */
  KRUISWINT, 
  
  /**
   * Status die aangeeft dat NUL heeft gewonnen
   */
  NULWINT, 
  
  /**
   * Status die aangeeft dat het spel zonder winnaar is geëindigd
   */
  GELIJK,
}

