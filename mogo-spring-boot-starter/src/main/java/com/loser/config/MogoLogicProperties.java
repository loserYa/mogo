package com.loser.config;

import com.loser.module.logic.entity.LogicProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mogo.logic")
public class MogoLogicProperties extends LogicProperty {

}
