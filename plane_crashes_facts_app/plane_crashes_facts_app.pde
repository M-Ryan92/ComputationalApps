import java.util.Collections;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.Calendar;

int bg = color(41, 52, 98);
int btnc = color(173, 145, 80);
int btnch = color(252, 233, 189);
int barc1 = color(161,113,42);
int barc2 = color(88,223,233);
int textc = 255;
String headerTitle = "Plane crash facts 1922-2014";

PFont openSans;
PFont xScale;

DrawUtil drawUtil;
Rows rows;
ArrayList<RowBoxes> rb = new ArrayList();
RowChart rc;
HashMap<String,HashMap<String,ArrayList<Accident>>> accidentsByTypeAndPhase,accidentsByPhaseAndYear;
HashMap<String, ArrayList<Accident>> accidentsByYear;
HashMap<String,ArrayList<Accident>> accidentsByType;
HashMap<String,ArrayList<Accident>> accidentsByPhase;
HashMap<String,Integer> colorsT;

void settings() {
  //size(900,400);
  fullScreen();
}

void setup() {
  openSans = createFont("open-sans.regular.ttf",14);
  xScale = createFont("X-SCALE.TTF",14);  
  rectMode(CORNER);
  drawUtil = new DrawUtil();
  drawUtil.setAppStyle(bg);
  rows = new Rows(3,20,5,new int[]{50,20});

  //organize all the data globaly;
  ArrayList<Accident> accidents;
  
  accidents = new ArrayList<Accident>();
  for(TableRow singleRow : loadTable ("AviationDataset.tsv", "header, tsv").rows()){
    accidents.add(new Accident(singleRow));
  }
  Collections.sort(accidents);
  
  accidentsByType = Util.groupAccidentsByType(accidents);
  accidentsByPhase = Util.groupAccidentsByPhase(accidents);
  accidentsByTypeAndPhase = Util.groupAccidentsByTypeAndPhase(accidents);
  accidentsByPhaseAndYear = Util.groupAccidentsByPhaseAndYear(accidents);
  accidentsByYear = Util.groupAccidentsByYear(accidents); 

  colorsT = new HashMap() {{
    int alpha = 255;
    put("COM", color(158, 166, 22, alpha));  
    put("MIL", color(52, 137, 15, alpha));
    put("INB", color(229, 66, 25, alpha));
    put("INH", color(119, 74, 74, alpha));
    put("EXG", color(191, 127, 63, alpha));
    put("EXS", color(36, 187, 198, alpha));    
  }};
  
  //data has been set now lets make the specific drawing classes (barchart and blocks)
  RowBoxes t = new RowBoxes("Types",rows.getRowBoundary(0),accidentsByType.keySet().toArray(), accidentsByType);  
  t.setDataColors(colorsT);
  rb.add(t);
  t = new RowBoxes("Phases",rows.getRowBoundary(1),accidentsByPhase.keySet().toArray(), accidentsByPhase);
  rb.add(t);
  basicDraw();
}

void basicDraw() {
  background(bg);
  drawUtil.drawTitle(headerTitle);
  fill(255);
  //dev testing boxes (borders to possition elements)
  //rows.drawRow(0);
  //rows.drawRow(1);
  //rows.drawRow(2);
  if(rc!=null) {
  rc.display();
  }
  
  for(RowBoxes rb1: rb){
    rb1.display();
  }
  if(rc==null) {
    rc = new RowChart("Dates",rows.getRowBoundary(2),accidentsByYear.keySet().toArray(), accidentsByYear);
    rc.display();
  }
  
}
boolean waitForIt = false;
boolean waitForIt2 = false;
float timeout;
void updateheaders(){
  boolean reset = false;
  for(RowBoxes r: rb){
    if(r.update()){
        reset=true;
        basicDraw();
      }
    }
  
    if(rc.update()){
      basicDraw();
    }
    if(reset){
      rb.get(0).resetConnections(true);
      rb.get(1).resetConnections(true);
      rc.resetConnections(true);
      waitForIt = false;
      waitForIt2 = false;      
    }

}

void draw() {
  updateheaders();
  if(!waitForIt){
    waitForIt = ((RowBoxes)rb.get(0)).connectAll(accidentsByTypeAndPhase, rb.get(1).getColBoxes());
    if(waitForIt) ((RowBoxes)rb.get(0)).resetConnections(true);
  } else if(!waitForIt2) {
    waitForIt2 = rc.connectAll(accidentsByPhaseAndYear, rb.get(1).getColBoxes(), colorsT);
  }

}