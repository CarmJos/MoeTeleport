package cc.carm.plugin.moeteleport.configuration.location;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.Objects;

public class DataLocation implements Cloneable {

	public static final DecimalFormat format = new DecimalFormat("0.00");
	private String worldName;
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;

	public DataLocation(Location location) {
		this(location.getWorld() != null ? location.getWorld().getName() : "", location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}

	public DataLocation(final String worldName, final double x, final double y, final double z) {
		this(worldName, x, y, z, 0, 0);
	}

	public DataLocation(final String worldName, final double x, final double y, final double z, final float yaw, final float pitch) {
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	public String getWorldName() {
		return worldName;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}


	public @NotNull Location getBukkitLocation(World world) {
		return new Location(world, getX(), getY(), getZ(), getYaw(), getPitch());
	}


	public @Nullable Location getBukkitLocation() {
		World world = Bukkit.getWorld(getWorldName());
		if (world == null) return null;
		else return new Location(world, getX(), getY(), getZ(), getYaw(), getPitch());
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DataLocation)) return false;
		DataLocation that = (DataLocation) o;
		return that.worldName.equals(worldName)
				&& Double.compare(that.x, x) == 0
				&& Double.compare(that.y, y) == 0
				&& Double.compare(that.z, z) == 0
				&& Float.compare(that.pitch, pitch) == 0
				&& Float.compare(that.yaw, yaw) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z, yaw, pitch);
	}

	@Override
	public String toString() {
		return worldName + " " + x + " " + y + " " + z + " " + yaw + " " + pitch;
	}

	public String toFlatString() {
		return worldName + "@" + format.format(x) + ", " + format.format(y) + ", " + format.format(z);
	}

	@Deprecated
	public String toSerializedString() {
		return serializeToText();
	}

	public String serializeToText() {
		if (getYaw() != 0 || getPitch() != 0) {
			return worldName + ";" + x + ";" + y + ";" + z + ";" + yaw + ";" + pitch;
		} else {
			return worldName + ";" + x + ";" + y + ";" + z;
		}
	}

	public static DataLocation deserializeText(String s) {
		if (s == null || !s.contains(";")) return null;
		String[] args = StringUtils.split(s, ";");
		if (args.length < 4) return null;
		try {
			String worldName = args[0];
			double x = NumberConversions.toDouble(args[0]);
			double y = NumberConversions.toDouble(args[1]);
			double z = NumberConversions.toDouble(args[2]);
			float yaw = 0;
			float pitch = 0;
			if (args.length == 5) {
				yaw = NumberConversions.toFloat(args[3]);
				pitch = NumberConversions.toFloat(args[4]);
			}
			return new DataLocation(worldName, x, y, z, yaw, pitch);
		} catch (Exception ex) {
			return null;
		}
	}

	@Deprecated
	public static DataLocation parseString(String s) {
		return deserializeText(s);
	}

}