package cn.eakoo;

import cn.eakoo.config.CustomSplashScreen;
import cn.eakoo.veiw.MainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author rui.zhou
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CodeGeneratorApplication extends AbstractJavaFxApplicationSupport {

	private Stage stage;

	public static void main(String[] args) {
		launch(CodeGeneratorApplication.class, MainView.class,new CustomSplashScreen(), args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("代码生成器");
		stage.setAlwaysOnTop(true);
		stage.setResizable(false);
		super.start(stage);
	}

}
