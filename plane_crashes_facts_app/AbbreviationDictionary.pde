static class AbbreviationDictionary{
  static HashMap<String, String> getAbreviationDic(){
    return abbreviations;
  }
  private static HashMap<String, String> abbreviations = new HashMap(){{
    put("COM", "COM : Commercial accident/incident");  
    put("MIL", "MIL : Military accident/incident");
    put("INB", "INB : Bombing");
    put("INH", "INH : Hijacking");
    put("EXG", "EXG : Attacked using ground-based weapons");
    put("EXS", "EXS : Attacked by other aircraft");
    
    put("STD", "Standing");
    put("TXI", "Taxi");
    put("TOF", "Take off");
    put("ICL", "Initial climb");
    put("ENR", "En route");
    put("MNV", "Maneuvering");
    put("APR", "Approach");
    put("LDG", "Landing");
    put("UNK", "Unknown");
    //put("", "");
  }};
}