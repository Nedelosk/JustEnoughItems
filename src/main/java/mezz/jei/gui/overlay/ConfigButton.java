package mezz.jei.gui.overlay;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;

import mezz.jei.Internal;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.config.Config;
import mezz.jei.config.Constants;
import mezz.jei.config.JEIModConfigGui;
import mezz.jei.gui.GuiHelper;
import mezz.jei.gui.TooltipRenderer;
import mezz.jei.gui.recipes.RecipesGui;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.HoverChecker;
import org.lwjgl.input.Keyboard;

public class ConfigButton {
	private final IngredientListOverlay parent;
	private final GuiButton configButton;
	private final IDrawable configButtonIcon;
	private final IDrawable configButtonCheatIcon;
	private final HoverChecker configButtonHoverChecker;

	public ConfigButton(IngredientListOverlay parent) {
		this.parent = parent;
		this.configButton = new GuiButton(2, 0, 0, 0, 0, "");
		ResourceLocation configButtonIconLocation = new ResourceLocation(Constants.RESOURCE_DOMAIN, Constants.TEXTURE_RECIPE_BACKGROUND_PATH);
		GuiHelper guiHelper = Internal.getHelpers().getGuiHelper();
		this.configButtonIcon = guiHelper.createDrawable(configButtonIconLocation, 0, 166, 16, 16);
		this.configButtonCheatIcon = guiHelper.createDrawable(configButtonIconLocation, 16, 166, 16, 16);
		this.configButtonHoverChecker = new HoverChecker(this.configButton, 0);
	}

	public void updateBounds(Rectangle area) {
		this.configButton.width = area.width;
		this.configButton.height = area.height;
		this.configButton.xPosition = area.x;
		this.configButton.yPosition = area.y;
	}

	public void draw(Minecraft minecraft, int mouseX, int mouseY) {
		this.configButton.drawButton(minecraft, mouseX, mouseY);

		IDrawable icon = Config.isCheatItemsEnabled() ? this.configButtonCheatIcon : this.configButtonIcon;
		icon.draw(minecraft, this.configButton.xPosition + 2, this.configButton.yPosition + 2);
	}

	public boolean isMouseOver(int mouseX, int mouseY) {
		return this.configButtonHoverChecker.checkHover(mouseX, mouseY);
	}

	public void drawTooltips(Minecraft minecraft, int mouseX, int mouseY) {
		if (isMouseOver(mouseX, mouseY)) {
			String configString = Translator.translateToLocal("jei.tooltip.config");
			if (Config.isCheatItemsEnabled()) {
				List<String> tooltip = Arrays.asList(
						configString,
						TextFormatting.RED + Translator.translateToLocal("jei.tooltip.cheat.mode")
				);
				TooltipRenderer.drawHoveringText(minecraft, tooltip, mouseX, mouseY);
			} else {
				TooltipRenderer.drawHoveringText(minecraft, configString, mouseX, mouseY);
			}
		}
	}

	public boolean handleMouseClick(Minecraft minecraft, int mouseX, int mouseY) {
		if (configButton.mousePressed(minecraft, mouseX, mouseY)) {
			configButton.playPressSound(minecraft.getSoundHandler());
			if (Keyboard.getEventKeyState() && (Keyboard.getEventKey() == Keyboard.KEY_LCONTROL || Keyboard.getEventKey() == Keyboard.KEY_RCONTROL)) {
				Config.toggleCheatItemsEnabled();
			} else {
				if (minecraft.currentScreen != null) {
					GuiScreen configScreen = new JEIModConfigGui(minecraft.currentScreen);
					parent.updateScreen(configScreen);
					RecipesGui.displayGuiScreenWithoutClose(configScreen);
				}
			}
			return true;
		}
		return false;
	}
}
