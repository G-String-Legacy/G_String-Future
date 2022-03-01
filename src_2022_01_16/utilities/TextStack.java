package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import application.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class TextStack
{
	/**
	 * Utility for G_String Help
	 * Reads html file and converts it to javafx Text
	 */

	private ArrayList<String> salLines = new ArrayList<String>();
	private Preferences prefs = null;
	private Popup popup;

	public TextStack(String sLocation, Preferences _prefs, Popup _popup)
	{
		prefs = _prefs;
		popup = _popup;
		popup.setObject(12, 69);
		try {
			readFile( sLocation );
		} catch (IOException e) {
			popup.tell("1200a", e);
		}
	}

	private void readFile(String filename) throws IOException {

		String sResource = "/resources/help/" + filename;
		InputStream stIn = Main.class.getResourceAsStream(sResource);
		//InputStream stIn = mainApp.class.getResourceAsStream("resources/help/Help_0.tf");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stIn));
        String line;
        while ((line = reader.readLine()) != null) {
            salLines.add(line);
        }
        reader.close();
	}

	public VBox vStack ()
	{
		String sTemp = null;
		String sOutput = null;
		String[] sOpts = {"title", "para", "bullet", "sub"};
		Text t = null;
		Image hand = null;
		InputStream stIn = Main.class.getResourceAsStream("/resources/Pointer.png");
		hand = new Image(stIn, 30.0, 30.0, true, true);
		ImageView ivHand = new ImageView(hand);
		ivHand.autosize();
		VBox vTemp = new VBox(10);
		vTemp.setMinWidth(600.0);
		vTemp.setPadding(new Insets(40.0, 40.0, 40.0, 40.0));
		vTemp.setStyle(prefs.get("Help Style","-fx-Background-color: Beige"));
		HBox wrapper = null;
		VBox bullet = null;
		bullet = new VBox();
		bullet.setPrefWidth(80.0);
		bullet.setAlignment(Pos.TOP_LEFT);
		//bullet.getChildren().add(tb);
        for (String sLine: salLines)
        {
        	for (String s : sOpts)
        	{
        		if (sLine.indexOf(s) == 1)			//that's a hit
        		{
        			sTemp = "<" + s + ">";
        			sOutput = sLine.replaceAll(sTemp, "");
        			sTemp = "</" + s + ">";
        			sOutput = sOutput.replaceFirst(sTemp, "");
        			wrapper = new HBox();
        			wrapper.setPrefWidth(600.0);
        			switch (s)
        			{
	        			case "title":
	        				DropShadow ds = new DropShadow();
	        				ds.setOffsetY(3.0f);
	        				ds.setColor(Color.rgb(80, 40, 13));
	            			t = new Text(sOutput + "\n");
	            			t.setFont(Font.font ("sans-serif", FontWeight.BOLD, 32));
	            			t.setFill(Color.rgb(80, 40, 13));
	            			t.setEffect(ds);
	            			wrapper.setAlignment(Pos.CENTER);
	            			wrapper.getChildren().add(t);
	        				break;
	        			case "para":
	            			t = new Text(sOutput + "\n");
	            			t.setFont(Font.font ("sans-serif", 16));
	        				t.setWrappingWidth(650.0);
	            			t.setTextAlignment(TextAlignment.JUSTIFY);
	            			t.setFill(Color.rgb(80, 40, 13));
	            			wrapper.getChildren().add(t);
	        				break;
	        			case "bullet":
	            			t = new Text(sOutput + "\n");
	            			t.setFont(Font.font ("sans-serif", 16));
	        				bullet = new VBox();
	        				bullet.setPrefWidth(40.0);
	            			t.setFill(Color.rgb(80, 40, 13));
	        				ivHand = new ImageView(hand);
	        				bullet.getChildren().add(ivHand);
	        				t.setWrappingWidth(650.0);
	            			t.setTextAlignment(TextAlignment.JUSTIFY);
	            			wrapper.getChildren().addAll(bullet, t);
	        				break;
	        			case "sub":
	        				Integer iPointer = sOutput.indexOf('|');
	        				String ss1 = sOutput.substring(0,  iPointer);
	        				String ss2 = " " + sOutput.substring(iPointer + 1);
	        				Text t1 = new Text(ss1);
	            			t1.setFont(Font.font ("sans-serif", FontWeight.BOLD, 17));
	        				Text t2 = new Text(ss2);
	            			t2.setFont(Font.font ("sans-serif", 16));
	        				t1.setWrappingWidth(650.0);
	            			t1.setTextAlignment(TextAlignment.JUSTIFY);
	            			t1.setFill(Color.rgb(80, 40, 13));
	        				t2.setWrappingWidth(650.0);
	            			t2.setTextAlignment(TextAlignment.JUSTIFY);
	            			t2.setFill(Color.rgb(80, 40, 13));
	            			TextFlow tf = new TextFlow(t1, t2);
	            			wrapper.getChildren().add(tf);
	            			break;
	    				default:
	    					break;
        			}
            		vTemp.getChildren().add(wrapper);
            		break;
    			}
        	}
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vTemp);
        scrollPane.setFitToWidth(true);

        VBox vBox = new VBox();
        vBox.getChildren().add(scrollPane);
		return vBox;
	}
}
