package com.github.mmonkey.Destinations.Services;

import java.io.File;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

import org.spongepowered.api.world.Location;

import com.flowpowered.math.vector.Vector3d;
import com.github.mmonkey.Destinations.Destination;
import com.github.mmonkey.Destinations.Destinations;
import com.github.mmonkey.Destinations.Utilities.DestinationTypes;

public class DestinationStorageService extends StorageService {

	public static final String DESTINATION = "destination";
	public static final String DESTINATION_TYPE = "type";
	public static final String WORLD = "world";
	public static final String LOCATION_X = "locationX";
	public static final String LOCATION_Y = "locationY";
	public static final String LOCATION_Z = "locationZ";
	public static final String ROTATION_X = "rotationX";
	public static final String ROTATION_Y = "rotationY";
	public static final String ROTATION_Z = "rotationZ";
	
	public void saveDestination(CommentedConfigurationNode config, Destination destination) {
		
		String type = destination.getType().name();
		String world = destination.getWorldName();
		Location location = destination.getLocation(getPlugin().getGame());
		Vector3d rotation = destination.getRotation();
		
		config.getNode(DESTINATION, DESTINATION_TYPE).setValue(type);
		config.getNode(DESTINATION, WORLD).setValue(world);
		config.getNode(DESTINATION, LOCATION_X).setValue(location.getX());
		config.getNode(DESTINATION, LOCATION_Y).setValue(location.getY());
		config.getNode(DESTINATION, LOCATION_Z).setValue(location.getZ());
		config.getNode(DESTINATION, ROTATION_X).setValue(rotation.getX());
		config.getNode(DESTINATION, ROTATION_Y).setValue(rotation.getY());
		config.getNode(DESTINATION, ROTATION_Z).setValue(rotation.getZ());
		
	}
	
	public Destination getDestination(CommentedConfigurationNode config) {
		
		CommentedConfigurationNode destinationConfig = (CommentedConfigurationNode) config.getNode(DESTINATION);
		
		return new Destination(
			(String) destinationConfig.getNode(WORLD).getString(),
			(Double) destinationConfig.getNode(LOCATION_X).getDouble(),
			(Double) destinationConfig.getNode(LOCATION_Y).getDouble(),
			(Double) destinationConfig.getNode(LOCATION_Z).getDouble(),
			(Double) destinationConfig.getNode(ROTATION_X).getDouble(),
			(Double) destinationConfig.getNode(ROTATION_Y).getDouble(),
			(Double) destinationConfig.getNode(ROTATION_Z).getDouble(),
			(DestinationTypes) DestinationTypes.valueOf((String) destinationConfig.getNode(DESTINATION_TYPE).getString())
		);
	}
	
	public DestinationStorageService(Destinations plugin, File configDir) {
		super(plugin, configDir);
	}

}
