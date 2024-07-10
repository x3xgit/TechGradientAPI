package techcommunity.techgradient;

import techcommunity.techgradient.gradient.GradientManager;

public class TechGradientAPI
{
    public static String applyGradient(String textInput)
    {
        return GradientManager.apply(textInput);
    }
}
