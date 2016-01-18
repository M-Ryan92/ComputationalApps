class RowChart extends RowBoxes{
  private int totalUnits=0;
  
  RowChart(String title, int[] offsets, Object[] types, HashMap<String,ArrayList<Accident>> values){
    super(title, offsets, types, values);
    super.setHasButton(false);
    
    for(Entry<String,ArrayList<Accident>> entry : values.entrySet()){
      totalUnits += entry.getValue().size();
    }
  }

   public void display(){
     int[] boundaryOffsets = super.boundaryOffsets;
     int columnOffsetY = super.columnOffsetY;
     int columnOffsetX = super.columnOffsetX;
     int headerHeight = super.headerHeight;
     int spacing = super.spacing;
     
     int y =  boundaryOffsets[1] + columnOffsetY + headerHeight + 5;
     int h = boundaryOffsets[3] - columnOffsetY - headerHeight - 5;
      
     float[] rect = new float[]{boundaryOffsets[0] + (2* spacing), y, 15};
     textSize(12);
     fill(textc);
     text("deaths legenda", rect[0],rect[1] - spacing);
     stroke(textc);
     fill(barc1);
     rect(rect[0],rect[1]+ rect[2],rect[2],rect[2]);
     fill(textc);
     text("ground", rect[0] + rect[2] + spacing ,rect[1] + rect[2] + spacing);
     
     fill(barc2);
     rect(rect[0],rect[1] + (2* rect[2]) + spacing,rect[2],rect[2]);
     fill(textc);
     text("passenger", rect[0] + rect[2] + spacing,rect[1] + (2* rect[2]) + (2*spacing));
     noStroke();
     super.drawHeader(60, 40);
     drawScale();
     drawColumn();
   }  
   
   private void drawScale(){
     int[] boundaryOffsets = super.boundaryOffsets;  
     int columnOffsetX = super.columnOffsetX;
     int columnOffsetY = super.columnOffsetY;
     int headerHeight = super.headerHeight;
     int h = boundaryOffsets[3] - headerHeight;
     range = new int[]{boundaryOffsets[1]+columnOffsetY + headerHeight,h};
     stroke(textc);
     rect(boundaryOffsets[0]+columnOffsetX-2,range[0]-headerHeight,1,range[1]+headerHeight);
     noStroke();
     fill(textc);
     int h0 = range[0] - 5;
     int h2000 = range[0] + range[1] - 5;
  
     textSize(10);
     for(int i=0; i <=2000;){
      float lp = map(i,0,2000,h0,h2000);
      String lt = String.valueOf(i);
      float lx = boundaryOffsets[0]+columnOffsetX- textWidth("0") - textWidth(lt);
      text(lt,lx, lp);
      stroke(textc);
      line(lx + textWidth(lt), lp+5, boundaryOffsets[2] + 10, lp+5);
      noStroke();
      i+=250;
     }   
   }
   
   int maxDepth;
   int[] range;
   private void drawColumn(){
     int[] boundaryOffsets = super.boundaryOffsets;
     int columnOffsetX = super.columnOffsetX;
     int columnOffsetY = super.columnOffsetY;
     int headerHeight = super.headerHeight;
     HashMap<String,Box> boxes = super.boxes;
     
     int spacing = super.spacing;
     if(super.boxes.size() == 0){
       
       //stroke(textc);
       int h = boundaryOffsets[3] - headerHeight;
       maxDepth = h;       
       float unitOffset = 0;
       int maxWidth = (boundaryOffsets[2]) - (boundaryOffsets[0]+ columnOffsetX) + (2*spacing);
       HashMap<String,Integer> tks = super.keySizes;

       for(Object o : super.values.keySet().toArray()){
         float size = map(tks.get(o), 0, totalUnits, -2, maxWidth);
         if(size < 7){
           maxWidth -= (7 - size);
           size = 7;
         }         
         int c = color(198, 194, 194, 100);
         float[] bounds = new float[]{
           boundaryOffsets[0]+ columnOffsetX + unitOffset,
           boundaryOffsets[1] + columnOffsetY,
           size,
           headerHeight
         };
          Box b = new Box(o.toString(), bounds, tks.get(o), c);
          boxes.put(o.toString(),b);
          unitOffset += size +2;
       }
     }
   }
   
   HashMap<String,Integer> colorsT;
   public boolean connectAll(HashMap<String,HashMap<String,ArrayList<Accident>>> results, HashMap<String,Box> connectBoxes, HashMap<String,Integer> colorsT){
     this.colorsT = colorsT;
     if(!super.wait){
       boolean prep = super.prepareConnector(connectBoxes);
       if(prep) return prep;   
     } else {
       Box my = super.yours;
       Box yours = super.my;
       HashMap<String,ArrayList<Accident>> r = results.get(my.getName());
       ArrayList<Accident> yearRes = r.get(yours.getName());
       if(yearRes !=null){
         float v = yearRes.size();
         FatalitiesCounter fc = new FatalitiesCounter(my.getName(), yours.getName(), r.get(yours.getName()).get(0).getType(), yearRes);
         super.tw = map(v, 1 , yours.getValue(), 0 , yours.getWidth());
         if(super.tw <= 3) super.tw = 3;
         super.wait = !connect(my, yours, super.tw, r.get(yours.getName()).get(0).getType(), fc);
       } else {
         super.tw=0;
         super.wait = false;
       }
     }
     
     return false;
   }
   int baroffset=0;
   Box current = null;
   boolean isconnected = false;
   public boolean connect(Box a, Box b, float size, String type, FatalitiesCounter fc){
     if(current == null){
       current = b;
     } else {
       if(!current.equals(b)){
       current = b;
       baroffset=0;
       }
     }
     isconnected =  a.connect(b, size, colorsT.get(type));
     if(isconnected){
     drawBars(b, size, fc);
     baroffset+=size;
     isconnected = false;
     return true;
     }
     return isconnected;
   }
   void drawBars(Box a, float size, FatalitiesCounter fc){
     fill(barc1);
     int max = 1700;
     float h =map(fc.getGround(),0,max,0,maxDepth);
     rect(a.getX()+baroffset,a.getY()+a.getHeigth(), size, h);
     
     fill(barc2);
     float h2 =map(fc.getPassengers(),0,max,0,maxDepth);
     rect(a.getX()+baroffset,a.getY()+a.getHeigth()+h, size, h2);
   }
}