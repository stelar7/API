package osu;

public enum OsuMods
{
	None(0),

	NoFail(1),

	Easy(2),

	NoVideo(4),
	Hidden(8),

	HardRock(16),

	SuddenDeath(32),

	DoubleTime(64),

	Relax(128),

	HalfTime(256),

	Nightcore(512),
	// Only set along with DoubleTime. i.e: NC only gives 576

	Flashlight(1024),

	Autoplay(2048),

	SpunOut(4096),

	Relax2(8192),
	// Autopilot?

	Perfect(16384),

	Key4(32768),

	Key5(65536),

	Key6(131072),

	Key7(262144),

	Key8(524288),

	FadeIn(1048576),

	Random(2097152),

	LastMod(4194304),

	Key9(16777216),

	Key10(33554432),

	Key1(67108864),

	Key3(134217728),

	Key2(268435456);

	int key;

	int keyMod()
	{
		return Key4.key | Key5.key | Key6.key | Key7.key | Key8.key;
	}

	int freemod()
	{
		return NoFail.key | Easy.key | Hidden.key | HardRock.key | SuddenDeath.key | Flashlight.key | FadeIn.key | Relax.key | Relax2.key | SpunOut.key | keyMod();
	}

	OsuMods(int key)
	{
		this.key = key;
	}
}
