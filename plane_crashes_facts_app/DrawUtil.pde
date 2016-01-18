public class DrawUtil { 
  
  void drawTitle(String title){
    textFont(xScale);
    textSize(45);
    fill(255);
    textAlign(CENTER,CENTER);
    text(title, width/2, 20);
    textAlign(LEFT,CENTER);    
    stroke(255);
    float x = (width/2)- (textWidth("Plane Crash Visualisation")/2);
    float w = (width/2)+ (textWidth("Plane Crash Visualisation")/2);
    if(x - 40 >= 0){
      x-=40;
    }
    if(w +40 <= width){
      w+=40;
    }
  
    line(x,42, w, 42);
    noStroke();
    textFont(openSans);
    textSize(12);
    text("press a key to fastForward", 20, 10);
    text("hover on a box to display the full name", 20, 30); 
  }
  void setAppStyle(int bg){    
    textFont(openSans);
    background(bg);  
  }
}