package pl.jlabs.beren.test;

import pl.jlabs.beren.annotations.Field;
import pl.jlabs.beren.annotations.Id;
import pl.jlabs.beren.annotations.Validate;
import pl.jlabs.beren.annotations.Validator;
import pl.jlabs.beren.test.model.fellowship.*;

import static pl.jlabs.beren.test.model.fellowship.Race.GOBLIN;
import static pl.jlabs.beren.test.model.fellowship.Race.ORC;

@Validator
public abstract class FellowshipValidator {

    @Validate(nullable = false,
            nullableMessage = "Fellowship was not assembled! Is it all lost already?",
            value = {
            @Field(name = "heroes", operation = "notEmpty"),
            @Field(name = "heroes", operation = "#forEachValue(checkMember)"),
            @Field(name = "heroesStuff", operation = "#forEachKey(neitherOf(['Saruman', 'Barlog', 'Gollum']))",
                    message = "Hey! Where did these things come from???"),
            @Field(name = "heroesStuff", operation = "#forEachValue(isThisGoodStuff)",
                    message = "I think we need to check our supplies before leaving..."),
            @Field(name = "elrondAdvices", operation = "whatDidElrondSaid")
    })
    abstract void checkFellowshipBeforeLeave(FellowshipOfTheRing fellowshipOfTheRing);

    boolean isThisGoodStuff(Inventory inventory) {
        return inventory != null && inventory.getValue() > 0.0 && inventory.getName() != null;
    }

    @Validate(value = {
            @Field(names = {"name", "homeland", "weapon"}, operation = "notNull", message = "Have you forgotten something like %{paramName}?"),
            @Field(type = Integer.class, operation = "greaterThan(0)"),
            @Field(name = "age", operation = "greaterThan(18)", message = "You must be adult to go for adventure!"),
            @Field(name = "race", operation = "spiesCheck", message = "There is a Sauron spy in our team!"),
    })
    abstract void checkMember(Hero hero);

    @Id("spiesCheck")
    boolean thisMethodIsRacist(Race race) {
        return !GOBLIN.equals(race) && !ORC.equals(race);
    }

    @Validate(value = {
            @Field(type = Object.class, operation = "notNull")
    })
    abstract void whatDidElrondSaid(VeryImportElrondAdvices advices);
}
