package timetableapp.gui;

import controlP5.Textfield;
import processing.core.PApplet;
import timetableapp.gui.drawHelper.DrawTable;
import timetableapp.models.DataManager;
import timetableapp.util.AppState;

public class DataView extends BaseView {

    private DataManager dm = DataManager.getInstance();
    private int btnWidth = 80;
    private DrawTable tableDrawer;

    public DataView() {
        super();
        ishidden = true;

        getControllers().add(cp5
                .addButton(cp5, "showPreviousColumns")
                .setColorBackground(AppState.buttonColor)
                .setPosition(app.width - (btnWidth * 2) - 30, app.height - 130)
                .setSize(btnWidth, state.getButtonHeight())
                .setLabel("Previous Columns")
                .hide());

        getControllers().add(cp5
                .addButton(cp5, "showNextColumns")
                .setColorBackground(AppState.buttonColor)
                .setPosition(app.width - btnWidth - 20, app.height - 130)
                .setSize(btnWidth, state.getButtonHeight())
                .setLabel("Next Columns")
                .hide());

        getControllers().add(cp5
                .addButton(cp5, "BackToMainView")
                .setColorBackground(AppState.buttonColor)
                .setPosition(20, app.height - 130)
                .setSize(btnWidth, state.getButtonHeight())
                .setLabel("Back")
                .hide());

        getControllers().add(cp5
                .addButton(cp5, "PreviousPage")
                .setColorBackground(AppState.buttonColor)
                .setPosition((app.width / 2) - (btnWidth / 2) - btnWidth - 10, app.height - 130)
                .setSize(btnWidth, state.getButtonHeight())
                .setLabel("Previous Page")
                .hide());

        getControllers().add(cp5
                .addButton(cp5, "NextPage")
                .setColorBackground(AppState.buttonColor)
                .setPosition((app.width / 2) - (btnWidth / 2) + btnWidth + 10, app.height - 130)
                .setSize(btnWidth, state.getButtonHeight())
                .setLabel("Next Page")
                .hide());

        getControllers().add(cp5
                .addTextfield("PageNr")
                .setColorBackground(AppState.buttonColor)
                .setValue("1")
                .setPosition((app.width / 2) - 30, app.height - 130)
                .setSize(60, state.getButtonHeight())
                .setFont(app.createFont("arial", 20))
                .setAutoClear(false)
                .hide());
        ((Textfield) getcontrollerByName("PageNr")).getCaptionLabel().alignX(PApplet.CENTER);
        //((Textfield) getcontrollerByName("PageNr")).getValueLabel().alignX(PApplet.CENTER);
        //((Textfield) getcontrollerByName("PageNr")).updateAbsolutePosition();
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
            app.fill(AppState.displayColor);
            app.rect(state.displayPanelXOffset, state.displayPanelYOffset, state.getDisplayPanelWidth(), state.getDisplayPanelHeight());
            app.fill(255);

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
            page+=1;
            tableDrawer.setPage(page);
            setPageBtnState();
            setPageNrToField(page);
            draw();
        }
    }

    public void pageMinus() {
        int page = tableDrawer.getPage();
        if (page > 0) {
            page-=1;
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
