package timetableapp.gui.views;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.Controller;
import controlP5.Textfield;
import processing.core.PApplet;
import timetableapp.gui.BaseView;
import timetableapp.gui.drawHelper.Draw;
import timetableapp.gui.drawHelper.DrawTable;
import timetableapp.models.DataManager;
import timetableapp.util.AppProperties;

public class DataView extends BaseView {

    private DataManager dm = DataManager.getInstance();
    private int btnWidth = 20;
    private DrawTable tableDrawer;
    private int yPos;

    public DataView() {
        super();
        ishidden = true;
        yPos = state.getDisplayPanelHeight() + AppProperties.buttonHeight;
        getControllers().add(cp5
                .addButton(cp5, "showPreviousColumns")
                .setColorBackground(AppProperties.buttonColor)
                .setPosition(app.width - (btnWidth * 3) - 10 - checklableWidth("columns"), yPos)//40 is the text within the 2 buttons
                .setSize(btnWidth, AppProperties.buttonHeight)
                .setLabel(Character.toString('\uf060'))
                .hide());

        getControllers().add(cp5
                .addButton(cp5, "showNextColumns")
                .setColorBackground(AppProperties.buttonColor)
                .setPosition(app.width - btnWidth - 20, yPos)
                .setSize(btnWidth, AppProperties.buttonHeight)
                .setLabel(Character.toString('\uf061'))
                .hide());

        getControllers().add(cp5
                .addButton(cp5, "BackToMainView")
                .setColorBackground(AppProperties.buttonColor)
                .setPosition(20, yPos)
                .setSize(70, AppProperties.buttonHeight)
                .setLabel("Back")
                .hide());

        getControllers().add(cp5
                .addButton(cp5, "PreviousPage")
                .setColorBackground(AppProperties.buttonColor)
                .setPosition((app.width / 2) - btnWidth - 35, yPos)
                .setSize(btnWidth, AppProperties.buttonHeight)
                .setLabel(Character.toString('\uf060'))
                .hide());

        getControllers().add(cp5
                .addButton(cp5, "NextPage")
                .setColorBackground(AppProperties.buttonColor)
                .setPosition((app.width / 2) + 35, yPos)
                .setSize(btnWidth, AppProperties.buttonHeight)
                .setLabel(Character.toString('\uf061'))
                .hide());

        getControllers().add(cp5
                .addTextfield("PageNr")
                .setColorBackground(AppProperties.buttonColor)
                .setValue("1")
                .setPosition((app.width / 2) - 30, yPos)
                .setSize(60, AppProperties.buttonHeight)
                .setFont(app.createFont("arial", 20))
                .setAutoClear(false)
                .hide());
        ((Textfield) getcontrollerByName("PageNr")).getCaptionLabel().alignX(PApplet.CENTER);
        ((Button) getcontrollerByName("PreviousPage")).getCaptionLabel().setFont(state.getIconFont());
        ((Button) getcontrollerByName("NextPage")).getCaptionLabel().setFont(state.getIconFont());
        ((Button) getcontrollerByName("showNextColumns")).getCaptionLabel().setFont(state.getIconFont());
        ((Button) getcontrollerByName("showPreviousColumns")).getCaptionLabel().setFont(state.getIconFont());

    }

    public void controlEvent(ControlEvent evt) {
        Controller<?> controller = evt.getController();
        switch (controller.getName()) {
            case ("NextPage"):
                pagePlus();
                break;
            case ("PreviousPage"):
                pageMinus();
                break;
            case ("showNextColumns"):
                colPagePlus();
                break;
            case ("showPreviousColumns"):
                colPageMinus();
                break;
        }
    }

    private int checklableWidth(String text) {
        int width;
        app.textFont(app.createFont("arial", 10));
        width = (int) app.textWidth(text) + 30;
        state.setFontSize();
        return width;
    }

    private void drawColLable() {
        if (getcontrollerByName("showNextColumns").isVisible() || getcontrollerByName("showPreviousColumns").isVisible()) {
            app.noStroke();
            app.fill(AppProperties.buttonColor);
            app.rect(app.width - (btnWidth * 2) - checklableWidth("Columns") - 4, yPos, checklableWidth("Columns"), AppProperties.buttonHeight);

            app.stroke(AppProperties.strokeColor);
            app.fill(AppProperties.textColor);
            app.textAlign(PApplet.LEFT);

            app.textFont(app.createFont("arial", 10));
            app.text("Columns", app.width - (btnWidth * 2) - checklableWidth("Columns"), yPos + AppProperties.displayPanelYOffset - 3);
            state.setFontSize();
            app.fill(255);
        }
    }

    public void handleEnter() {
        int maxPages = dm.getTm().getPageCount();
        int selectedPage = getPageNrFromField();
        int page = tableDrawer.getPage();

        if (page != selectedPage
                && selectedPage >= 0
                && selectedPage <= maxPages) {
            tableDrawer.setPage(selectedPage);
            setPageBtnState();
        } else {
            setPageNrToField(page);
        }
    }

    @Override
    public void show() {
        if (tableDrawer == null) {
            tableDrawer = new DrawTable();
        }
        super.show();
    }

    @Override
    public void draw() {
        if (ishidden == false) {
            Draw.drawDisplay();
            drawColLable();
            tableDrawer.draw();
        }
    }

    public void resetTable() {
        tableDrawer.setColPage(0);
        tableDrawer.setPage(0);
        setPageNrToField(0);
    }

    public void colPagePlus() {
        int colPage = tableDrawer.getColPage();
        if (colPage + 1 < tableDrawer.getColSlidesCount()) {
            tableDrawer.setColPage(colPage + 1);
            setColPageBtnState();
            draw();
        }
    }

    public void colPageMinus() {
        int colPage = tableDrawer.getColPage();
        if (colPage > 0) {
            tableDrawer.setColPage(colPage - 1);
            setColPageBtnState();
            draw();
        }
    }

    public void setColPageBtnState() {
        int colPage = tableDrawer.getColPage();
        if (colPage + 1 >= tableDrawer.getColSlidesCount()) {
            getcontrollerByName("showNextColumns").hide();
        } else {
            getcontrollerByName("showNextColumns").show();
        }
        if (colPage == 0) {
            getcontrollerByName("showPreviousColumns").hide();
        } else {
            getcontrollerByName("showPreviousColumns").show();
        }
    }

    public void pagePlus() {
        int page = tableDrawer.getPage();

        if (page < dm.getTm().getPageCount()) {
            page += 1;
            tableDrawer.setPage(page);
            setPageBtnState();
            setPageNrToField(page);
            draw();
        }
    }

    public void pageMinus() {
        int page = tableDrawer.getPage();
        if (page > 0) {
            page -= 1;
            tableDrawer.setPage(page);
            setPageBtnState();
            setPageNrToField(page);
            draw();
        }
    }

    public void setPageBtnState() {
        int page = tableDrawer.getPage();
        if (page >= dm.getTm().getPageCount()) {
            getcontrollerByName("NextPage").hide();
        } else {
            getcontrollerByName("NextPage").show();
        }
        if (page == 0) {
            getcontrollerByName("PreviousPage").hide();
        } else {
            getcontrollerByName("PreviousPage").show();
        }
    }

    public void setPageNrToField(int p) {
        ((Textfield) getcontrollerByName("PageNr")).setText(String.valueOf(p + 1));
    }

    public int getPageNrFromField() {
        try {
            return Integer.valueOf(((Textfield) getcontrollerByName("PageNr")).getText()) - 1;
        } catch (Exception e) {
            return tableDrawer.getPage();
        }
    }

}
