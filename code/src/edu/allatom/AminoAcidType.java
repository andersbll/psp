package edu.allatom;

import java.util.*;

import edu.math.Point;

// AminoAcid types specification
enum AminoAcidType {
	ALA (
			0,
			new Atom[]{
					new Atom(Atom.Type.H, "HB3", new Point(1.8984307f, 0.015287995f, -0.007171273f)),
					new Atom(Atom.Type.H, "HB2", new Point(1.9103465f, 0.004295826f, 0.001791209f)),
					new Atom(Atom.Type.H, "HA", new Point(-0.3563828f, -0.71022636f, -0.5484213f)),
					new Atom(Atom.Type.C, "C", new Point(-0.5380467f, -0.17861983f, 1.2316666f)),
					new Atom(Atom.Type.N, "N", new Point(-0.45466685f, 1.1101208f, -0.47548872f)),
					new Atom(Atom.Type.C, "CB", new Point(1.539988f, -1.4901161E-8f, 4.0978193E-8f)),
					new Atom(Atom.Type.O, "O", new Point(-0.96291864f, -1.1384184f, 1.5354611f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.H, "HB1", new Point(1.9092338f, -0.0045806468f, -0.011174381f)),
					new Atom(Atom.Type.H, "H", new Point(0.2107803f, 1.7220597f, -0.7106306f)),
			},
			new String[][]{{"CA","CB"},{"CA","HA"},{"CB","HB1"},{"CB","HB2"},{"CB","HB3"}}
	),
	ARG (
			4,
			new Atom[]{
					new Atom(Atom.Type.C, "CZ", new Point(-0.37544814f, 0.56027746f, -0.017632866f)),
					new Atom(Atom.Type.H, "HD2", new Point(1.056374f, 3.1146188f, 0.8409434f)),
					new Atom(Atom.Type.H, "HD3", new Point(1.0742875f, 3.1244009f, -0.831908f)),
					new Atom(Atom.Type.H, "HB3", new Point(1.9036814f, -0.53643f, -0.8744766f)),
					new Atom(Atom.Type.H, "HB2", new Point(1.8906834f, -0.5280658f, 0.88601255f)),
					new Atom(Atom.Type.H, "HE", new Point(-1.0421867f, 2.4506066f, -0.012078984f)),
					new Atom(Atom.Type.N, "NH1", new Point(0.6433128f, -0.23890153f, -0.014702952f)),
					new Atom(Atom.Type.C, "C", new Point(-0.5355225f, -0.10798472f, 0.5158807f)),
					new Atom(Atom.Type.H, "HA", new Point(-0.35339427f, -0.28360578f, -0.23492327f)),
					new Atom(Atom.Type.H, "HH21", new Point(-2.3025465f, 0.6745475f, -0.026077129f)),
					new Atom(Atom.Type.N, "N", new Point(-0.4833972f, 0.46453363f, -0.17985764f)),
					new Atom(Atom.Type.C, "CB", new Point(1.5529127f, 1.8626451E-9f, -3.3527613E-8f)),
					new Atom(Atom.Type.O, "O", new Point(-1.0322281f, -0.49725437f, 0.62209934f)),
					new Atom(Atom.Type.N, "NH2", new Point(-1.5216671f, 0.06772217f, -0.025147393f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.H, "H", new Point(0.14910278f, 0.7272794f, -0.32036102f)),
					new Atom(Atom.Type.H, "HH22", new Point(-1.6209965f, -0.916125f, -0.030417502f)),
					new Atom(Atom.Type.C, "CG", new Point(2.139845f, 1.4660065f, 5.9604645E-8f)),
					new Atom(Atom.Type.H, "HH12", new Point(0.5397807f, -1.2220974f, -0.02004344f)),
					new Atom(Atom.Type.H, "HH11", new Point(1.5242354f, 0.13375983f, -0.0074163433f)),
					new Atom(Atom.Type.C, "CD", new Point(1.0022529f, 2.5156004f, 1.8835999E-7f)),
					new Atom(Atom.Type.H, "HG3", new Point(2.7531648f, 1.612161f, -0.8833586f)),
					new Atom(Atom.Type.H, "HG2", new Point(2.754526f, 1.6261079f, 0.87693137f)),
					new Atom(Atom.Type.N, "NE", new Point(-0.2563241f, 1.8837262f, -0.011592612f)),
			},
			new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD","CG"},{"CD","HD2"},{"CD","HD3"},{"CD","NE"},{"CG","HG2"},{"CG","HG3"},{"CZ","NE"},{"CZ","NH1"},{"CZ","NH2"},{"H11","NH1"},{"H12","NH1"},{"H21","NH2"},{"H22","NH2"},{"HE","NE"}}
	),
	ASN (
			2,
			new Atom[]{
					new Atom(Atom.Type.O, "OD1", new Point(3.3163455f, 1.6475959f, 0.015516162f)),
					new Atom(Atom.Type.H, "HB3", new Point(1.9269238f, -0.5114804f, -0.880898f)),
					new Atom(Atom.Type.H, "HB2", new Point(1.9281049f, -0.5143484f, 0.87864184f)),
					new Atom(Atom.Type.H, "HD21", new Point(0.29791433f, 2.263011f, 0.002514869f)),
					new Atom(Atom.Type.H, "HD22", new Point(1.5956094f, 3.3569427f, 0.001990825f)),
					new Atom(Atom.Type.N, "ND2", new Point(1.2630988f, 2.4359603f, 0.0f)),
					new Atom(Atom.Type.C, "C", new Point(-0.5365483f, -0.7665921f, 0.23850876f)),
					new Atom(Atom.Type.H, "HA", new Point(-0.3639652f, 0.114084154f, -0.5573696f)),
					new Atom(Atom.Type.C, "CB", new Point(1.5597218f, 1.4901161E-8f, 1.4901161E-8f)),
					new Atom(Atom.Type.N, "N", new Point(-0.48804256f, 0.5818788f, 0.54931813f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.O, "O", new Point(-1.0144274f, -0.21048051f, 0.099610984f)),
					new Atom(Atom.Type.H, "H", new Point(-0.6495756f, 1.9688237f, 0.44353706f)),
					new Atom(Atom.Type.C, "CG", new Point(2.1018245f, 1.4364347f, -2.980232E-8f)),
			},
			new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CG","ND2"},{"CG","OD1"},{"HD21","ND2"},{"HD22","ND2"}}
	),
	ASP (
			2,
			new Atom[]{
					new Atom(Atom.Type.O, "OD1", new Point(1.3084519f, 2.358934f, -1.0430813E-7f)),
					new Atom(Atom.Type.O, "OD2", new Point(3.3171043f, 1.5833812f, 0.0014853363f)),
					new Atom(Atom.Type.H, "HB3", new Point(1.9178193f, -0.5184998f, -0.87764037f)),
					new Atom(Atom.Type.C, "CG", new Point(2.105615f, 1.4340434f, -4.5563166E-8f)),
					new Atom(Atom.Type.H, "HB2", new Point(1.9088414f, -0.51290274f, 0.8829263f)),
					new Atom(Atom.Type.H, "HA", new Point(-0.3528465f, 0.0661587f, -0.10683845f)),
					new Atom(Atom.Type.C, "C", new Point(-0.5343537f, -0.16957945f, 6.153354E-4f)),
					new Atom(Atom.Type.N, "N", new Point(-0.467587f, 0.074085675f, 0.14822015f)),
					new Atom(Atom.Type.C, "CB", new Point(1.5522105f, -2.6077032E-8f, -2.2924864E-9f)),
					new Atom(Atom.Type.O, "O", new Point(-0.6917944f, -0.30038118f, -0.09053754f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.H, "H", new Point(0.03767878f, 0.19501129f, 0.16318467f)),
			},
			new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CG","OD1"},{"CG","OD2"}}
	),
	CYS (
			1,
			new Atom[]{
			},//TODO
			new String[][]{{"CA","CB"},{"CA","HA"},{"CB","HB2"},{"CB","HB3"},{"CB","SG"},{"HG","SG"}}
	),
	GLN (
			3,
			new Atom[]{
					new Atom(Atom.Type.H, "HB3", new Point(1.9253134f, -0.5257821f, -0.8779933f)),
					new Atom(Atom.Type.H, "HB2", new Point(1.9109616f, -0.52351564f, 0.88200307f)),
					new Atom(Atom.Type.N, "NE2", new Point(-0.2353897f, 2.1064181f, -1.1920929E-7f)),
					new Atom(Atom.Type.C, "C", new Point(-0.51979643f, -0.19975267f, -0.15330787f)),
					new Atom(Atom.Type.H, "HA", new Point(-0.35282263f, 0.16610171f, -0.075453855f)),
					new Atom(Atom.Type.N, "N", new Point(-0.49223724f, -0.027624449f, 0.23624879f)),
					new Atom(Atom.Type.C, "CB", new Point(1.5594448f, -8.692344E-9f, 1.9868216E-8f)),
					new Atom(Atom.Type.O, "O", new Point(-0.9863785f, -0.80582905f, -0.26001248f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.H, "H", new Point(0.0674058f, -0.12339767f, 0.25933853f)),
					new Atom(Atom.Type.C, "CG", new Point(2.13097f, 1.4529682f, 0.0f)),
					new Atom(Atom.Type.C, "CD", new Point(1.0063862f, 2.4949102f, -8.133551E-8f)),
					new Atom(Atom.Type.O, "OE1", new Point(1.2737821f, 3.6951172f, -0.024617076f)),
					new Atom(Atom.Type.H, "HG3", new Point(2.7439792f, 1.5969232f, -0.87606835f)),
					new Atom(Atom.Type.H, "HG2", new Point(2.745587f, 1.601409f, 0.8731081f)),
					new Atom(Atom.Type.H, "HE22", new Point(-0.95292693f, 2.7675352f, -0.0038419564f)),
					new Atom(Atom.Type.H, "HE21", new Point(-0.44900092f, 1.152188f, -7.9524517E-4f)),
			},
			new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD","CG"},{"CD","NE2"},{"CD","OE1"},{"CG","HG2"},{"CG","HG3"},{"E21","NE2"},{"E22","NE2"}}
	),
	GLU (
			3,
			new Atom[]{
					new Atom(Atom.Type.H, "HB3", new Point(1.9213222f, -0.51975113f, -0.88140714f)),
					new Atom(Atom.Type.H, "HB2", new Point(1.905295f, -0.52061844f, 0.884658f)),
					new Atom(Atom.Type.C, "C", new Point(-0.5342626f, -0.6078033f, 0.23331103f)),
					new Atom(Atom.Type.H, "HA", new Point(-0.35226268f, 0.0634035f, -0.463286f)),
					new Atom(Atom.Type.C, "CB", new Point(1.5535696f, 0.0f, -3.0733645E-8f)),
					new Atom(Atom.Type.N, "N", new Point(-0.48189765f, 0.50605273f, 0.38078442f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.O, "O", new Point(-1.0397496f, -0.876651f, 0.079088636f)),
					new Atom(Atom.Type.H, "H", new Point(0.16281494f, 0.7505763f, 0.63595283f)),
					new Atom(Atom.Type.C, "CG", new Point(2.1069953f, 1.4577291f, -1.7136335E-8f)),
					new Atom(Atom.Type.C, "CD", new Point(0.966317f, 2.4899619f, 2.225861E-8f)),
					new Atom(Atom.Type.O, "OE1", new Point(-0.18324909f, 2.0783772f, 3.576279E-8f)),
					new Atom(Atom.Type.H, "HG3", new Point(2.7194638f, 1.6069028f, -0.87811124f)),
					new Atom(Atom.Type.H, "HG2", new Point(2.7200966f, 1.6106765f, 0.8783227f)),
					new Atom(Atom.Type.O, "OE2", new Point(1.2619245f, 3.6744132f, -7.763609E-4f)),
			},
			new String[][]{{"CA","HA"},{"CB","CA"},{"CB","CG"},{"CB","HB3"},{"CD","OE1"},{"CD","OE2"},{"CG","CD"},{"CG","HG2"},{"H","N"},{"HB2","CB"},{"HG3","CG"}}
	),
	GLY (
			0,
			new Atom[]{
					new Atom(Atom.Type.H, "HA2", new Point(-0.3504521f, -0.0021617461f, 0.3219475f)),
					new Atom(Atom.Type.H, "HA3", new Point(1.0813588f, 9.934108E-9f, -8.381903E-9f)),
					new Atom(Atom.Type.C, "C", new Point(-0.49111804f, 0.41056278f, -0.2099415f)),
					new Atom(Atom.Type.N, "N", new Point(-0.49971095f, -0.38578907f, -0.22318263f)),
					new Atom(Atom.Type.O, "O", new Point(-0.22068949f, 0.6620254f, -0.3661594f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.H, "H", new Point(-0.68066025f, -0.55785584f, -0.18243046f)),
			},
			new String[][]{{"CA","HA2"},{"CA","HA3"}}
	),
	HIS (
			2,
			new Atom[]{
					new Atom(Atom.Type.H, "HD2", new Point(0.3439863f, 2.757703f, 0.004109174f)),
					new Atom(Atom.Type.N, "ND1", new Point(3.411559f, 1.7083704f, -0.004401326f)),
					new Atom(Atom.Type.H, "HB3", new Point(1.9175348f, -0.50469744f, -0.8796388f)),
					new Atom(Atom.Type.H, "HB2", new Point(1.914213f, -0.5092035f, 0.88082975f)),
					new Atom(Atom.Type.H, "HE1", new Point(4.4821663f, 3.555643f, -2.630949E-4f)),
					new Atom(Atom.Type.H, "HE2", new Point(2.1723218f, 4.615997f, -0.0065353513f)),
					new Atom(Atom.Type.N, "NE2", new Point(2.3484905f, 3.652972f, -0.0051211566f)),
					new Atom(Atom.Type.C, "C", new Point(-0.53829324f, -0.80090994f, 0.8810936f)),
					new Atom(Atom.Type.H, "HA", new Point(-0.35534066f, -0.28053847f, -0.80321586f)),
					new Atom(Atom.Type.N, "N", new Point(-0.48711812f, 1.1268216f, 0.19656307f)),
					new Atom(Atom.Type.C, "CB", new Point(1.5499028f, 0.0f, 0.0f)),
					new Atom(Atom.Type.O, "O", new Point(-1.0167354f, -1.1928076f, 0.61272556f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.H, "H", new Point(0.1523495f, 1.730339f, 0.3312041f)),
					new Atom(Atom.Type.C, "CG", new Point(2.0612526f, 1.4183229f, -6.426126E-8f)),
					new Atom(Atom.Type.C, "CE1", new Point(3.5366921f, 3.0424094f, -0.0067913234f)),
					new Atom(Atom.Type.C, "CD2", new Point(1.4147894f, 2.6264033f, -1.1920929E-7f)),
			},
			new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD2","CG"},{"CD2","HD2"},{"CD2","NE2"},{"CE1","HE1"},{"CE1","ND1"},{"CE1","NE2"},{"CG","ND1"}} //FIXME abll: jeg mener at {"HE2","NE2"} skal tilføjes
	),
	ILE (
			2,
			new Atom[]{
					new Atom(Atom.Type.H, "HG21", new Point(1.4294552f, -1.1062807f, 1.3550502f)),
					new Atom(Atom.Type.H, "HB", new Point(1.9106356f, -0.49945474f, 0.9154367f)),
					new Atom(Atom.Type.H, "HD13", new Point(0.8078904f, 2.811066f, 0.62304235f)),
					new Atom(Atom.Type.H, "HG22", new Point(1.2545763f, -0.11929965f, 0.92578524f)),
					new Atom(Atom.Type.H, "HD11", new Point(0.35679233f, 2.3466854f, -0.25113717f)),
					new Atom(Atom.Type.C, "C", new Point(-0.5476425f, 0.90775245f, -0.72979647f)),
					new Atom(Atom.Type.H, "HA", new Point(-0.35531425f, 0.13332242f, 0.83376294f)),
					new Atom(Atom.Type.H, "HD12", new Point(1.0925967f, 3.1348913f, -0.37164354f)),
					new Atom(Atom.Type.N, "N", new Point(-0.4889374f, -1.0808355f, -0.3843411f)),
					new Atom(Atom.Type.C, "CB", new Point(1.5849366f, -1.9868216E-8f, -3.973643E-8f)),
					new Atom(Atom.Type.O, "O", new Point(-0.9135616f, 1.7698054f, -0.3689861f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.H, "H", new Point(0.13008447f, -1.6927763f, -0.5981004f)),
					new Atom(Atom.Type.C, "CG1", new Point(2.1476452f, 1.480955f, -9.934098E-9f)),
					new Atom(Atom.Type.C, "CG2", new Point(2.1865141f, -0.79351765f, -1.2146212f)),
					new Atom(Atom.Type.H, "HG23", new Point(1.8999834f, -0.8445466f, 1.6438395f)),
					new Atom(Atom.Type.C, "CD1", new Point(1.014986f, 2.5194347f, 5.9604645E-8f)),
					new Atom(Atom.Type.H, "HG13", new Point(2.7626708f, 1.636232f, -0.86860394f)),
					new Atom(Atom.Type.H, "HG12", new Point(2.7555873f, 1.6410934f, 0.87976056f)),
			},
			new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG1"},{"CB","CG2"},{"CB","HB"},{"CD1","CG1"},{"CD1","HD11"},{"CD1","HD12"},{"CD1","HD13"},{"CG1","HG12"},{"CG1","HG13"},{"CG2","HG21"},{"CG2","HG22"},{"CG2","HG23"}}
	),
	LEU (
			2,
			new Atom[]{
					new Atom(Atom.Type.H, "HG", new Point(2.7502675f, 1.5992037f, -0.9043477f)),
					new Atom(Atom.Type.H, "HB3", new Point(1.9218565f, -0.5310395f, -0.8759177f)),
					new Atom(Atom.Type.H, "HD21", new Point(3.0196595f, 1.4764559f, 1.7990954f)),
					new Atom(Atom.Type.H, "HB2", new Point(1.9099892f, -0.53240895f, 0.8803291f)),
					new Atom(Atom.Type.H, "HD22", new Point(3.3028066f, 2.2354565f, 1.4639043f)),
					new Atom(Atom.Type.H, "HD13", new Point(0.8157354f, 2.8465528f, -0.3208496f)),
					new Atom(Atom.Type.H, "HD23", new Point(3.7214606f, 1.4953572f, 1.2768624f)),
					new Atom(Atom.Type.H, "HD11", new Point(0.9306999f, 2.9725502f, 0.21930146f)),
					new Atom(Atom.Type.C, "C", new Point(-0.5407017f, -0.25630835f, 0.99030507f)),
					new Atom(Atom.Type.H, "HA", new Point(-0.35522646f, -0.5339659f, -0.49875674f)),
					new Atom(Atom.Type.H, "HD12", new Point(0.5543735f, 2.570838f, 0.089804806f)),
					new Atom(Atom.Type.N, "N", new Point(-0.5009673f, 0.94314355f, -0.31197593f)),
					new Atom(Atom.Type.C, "CB", new Point(1.564932f, 6.6227384E-9f, 9.934108E-9f)),
					new Atom(Atom.Type.O, "O", new Point(-0.6706578f, -0.5297383f, 1.4391305f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.H, "H", new Point(0.019170268f, 1.3941305f, -0.34009972f)),
					new Atom(Atom.Type.C, "CG", new Point(2.1706095f, 1.4644903f, -3.3113676E-9f)),
					new Atom(Atom.Type.C, "CD2", new Point(3.1211028f, 1.682629f, 1.2273533f)),
					new Atom(Atom.Type.C, "CD1", new Point(1.0336741f, 2.540317f, -2.6283992E-8f)),
			},
			new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD1","CG"},{"CD1","HD11"},{"CD1","HD12"},{"CD1","HD13"},{"CD2","CG"},{"CD2","HD21"},{"CD2","HD22"},{"CD2","HD23"},{"CG","HG"}}
	),
	LYS (
			4,
			new Atom[]{
					new Atom(Atom.Type.H, "HD2", new Point(1.071512f, 3.1337433f, 0.8849741f)),
					new Atom(Atom.Type.H, "HD3", new Point(1.0681207f, 3.1446888f, -0.8752409f)),
					new Atom(Atom.Type.H, "HB3", new Point(1.9104984f, -0.5308613f, -0.8751728f)),
					new Atom(Atom.Type.N, "NZ", new Point(-0.22388797f, 0.35981795f, 1.6223639E-7f)),
					new Atom(Atom.Type.H, "HB2", new Point(1.8977177f, -0.51959336f, 0.8909296f)),
					new Atom(Atom.Type.H, "HE2", new Point(-0.97127897f, 2.1182907f, 0.8852417f)),
					new Atom(Atom.Type.H, "HE3", new Point(-0.97041667f, 2.1190283f, -0.88275033f)),
					new Atom(Atom.Type.C, "C", new Point(-0.53123546f, -0.44403562f, 0.73643005f)),
					new Atom(Atom.Type.H, "HA", new Point(-0.35976005f, -0.31096813f, -0.53717434f)),
					new Atom(Atom.Type.H, "HZ1", new Point(0.114261486f, 0.056974053f, -0.30125472f)),
					new Atom(Atom.Type.N, "N", new Point(-0.46723804f, 0.8415923f, -0.012290781f)),
					new Atom(Atom.Type.C, "CB", new Point(1.5523022f, -2.0861625E-8f, 0.0f)),
					new Atom(Atom.Type.O, "O", new Point(-0.6575479f, -1.262771f, 1.061837f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.H, "H", new Point(0.17302111f, 1.2117846f, 0.042918194f)),
					new Atom(Atom.Type.C, "CG", new Point(2.127845f, 1.4545763f, 3.5762792E-8f)),
					new Atom(Atom.Type.H, "HZ2", new Point(-0.5859125f, -0.031538676f, -0.10492869f)),
					new Atom(Atom.Type.C, "CE", new Point(-0.41379538f, 1.83295f, 1.5757978E-7f)),
					new Atom(Atom.Type.H, "HZ3", new Point(-0.06447263f, 0.02887969f, 0.40835485f)),
					new Atom(Atom.Type.C, "CD", new Point(0.9816574f, 2.5133922f, 4.321337E-8f)),
					new Atom(Atom.Type.H, "HG3", new Point(2.7415595f, 1.5994806f, -0.88292754f)),
					new Atom(Atom.Type.H, "HG2", new Point(2.7470908f, 1.597155f, 0.8758384f)),
			},
			new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD","CE"},{"CD","CG"},{"CD","HD2"},{"CD","HD3"},{"CE","HE2"},{"CE","HE3"},{"CE","NZ"},{"CG","HG2"},{"CG","HG3"},{"HZ1","NZ"},{"HZ2","NZ"},{"HZ3","NZ"}}
	),
	MET (
			3,
			new Atom[]{
					new Atom(Atom.Type.S, "SD", new Point(0.7800882f, 2.663955f, 1.1920929E-7f)),
					new Atom(Atom.Type.H, "HB3", new Point(1.8984662f, -0.52437824f, -0.88439244f)),
					new Atom(Atom.Type.H, "HB2", new Point(1.8893023f, -0.5147887f, 0.8966807f)),
					new Atom(Atom.Type.H, "HE1", new Point(-0.71104145f, 1.066392f, 0.9644835f)),
					new Atom(Atom.Type.H, "HE2", new Point(-0.5024247f, 0.79661894f, -0.76030946f)),
					new Atom(Atom.Type.H, "HE3", new Point(-1.5441964f, 2.098203f, -0.19986677f)),
					new Atom(Atom.Type.C, "C", new Point(-0.50321275f, -0.043820113f, 1.4386852f)),
					new Atom(Atom.Type.H, "HA", new Point(-0.35550442f, -0.877545f, -0.5177092f)),
					new Atom(Atom.Type.N, "N", new Point(-0.48830673f, 1.1954566f, -0.68066126f)),
					new Atom(Atom.Type.C, "CB", new Point(1.543301f, 0.0f, 8.940697E-8f)),
					new Atom(Atom.Type.O, "O", new Point(-0.8034188f, -1.109769f, 1.9569516f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.H, "H", new Point(0.17953527f, 1.8290517f, -1.0370908f)),
					new Atom(Atom.Type.C, "CG", new Point(2.127556f, 1.4308264f, 1.7881393E-7f)),
					new Atom(Atom.Type.C, "CE", new Point(-0.6410806f, 1.5441554f, 0.0f)),
					new Atom(Atom.Type.H, "HG3", new Point(2.7251253f, 1.567364f, -0.89128375f)),
					new Atom(Atom.Type.H, "HG2", new Point(2.7627268f, 1.5554309f, 0.8528357f)),
			},
			new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CE","HE1"},{"CE","HE2"},{"CE","HE3"},{"CE","SD"},{"CG","HG2"},{"CG","HG3"},{"CG","SD"}}
	),
	PHE (
			2,
			new Atom[]{
					new Atom(Atom.Type.H, "HD2", new Point(4.141063f, 0.8593365f, -0.003221631f)),
					new Atom(Atom.Type.C, "CZ", new Point(3.0506501f, 4.0775385f, -0.015757859f)),
					new Atom(Atom.Type.H, "HB3", new Point(1.9185505f, -0.51949674f, -0.8805918f)),
					new Atom(Atom.Type.H, "HD1", new Point(0.114641905f, 2.3500562f, 0.010414124f)),
					new Atom(Atom.Type.H, "HB2", new Point(1.9277527f, -0.52292836f, 0.8773123f)),
					new Atom(Atom.Type.H, "HE1", new Point(0.9759353f, 4.6695776f, -0.011571348f)),
					new Atom(Atom.Type.H, "HE2", new Point(5.016878f, 3.1778615f, -0.012273073f)),
					new Atom(Atom.Type.C, "C", new Point(-0.5551721f, 0.27723452f, -0.031856954f)),
					new Atom(Atom.Type.H, "HA", new Point(-0.32608217f, -0.036518514f, 0.19027303f)),
					new Atom(Atom.Type.C, "CE2", new Point(3.9462035f, 2.9996493f, -0.010735691f)),
					new Atom(Atom.Type.N, "N", new Point(-0.5142788f, -0.17614654f, -0.18506116f)),
					new Atom(Atom.Type.C, "CB", new Point(1.5596311f, 0.0f, 5.9604645E-8f)),
					new Atom(Atom.Type.O, "O", new Point(-0.8762169f, 0.42181885f, 0.13583803f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.H, "H", new Point(0.034212135f, -0.57677877f, -0.46829116f)),
					new Atom(Atom.Type.H, "HZ", new Point(3.4244413f, 5.0948143f, -0.025958896f)),
					new Atom(Atom.Type.C, "CG", new Point(2.0823956f, 1.4426069f, -3.0035157E-8f)),
					new Atom(Atom.Type.C, "CE1", new Point(1.6692288f, 3.8392267f, -0.010829091f)),
					new Atom(Atom.Type.C, "CD2", new Point(3.459166f, 1.6877842f, -0.006403208f)),
					new Atom(Atom.Type.C, "CD1", new Point(1.1829197f, 2.528405f, -2.3841858E-7f)),
				},
			new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD1","CE1"},{"CD1","CG"},{"CD1","HD1"},{"CD2","CE2"},{"CD2","CG"},{"CD2","HD2"},{"CE1","CZ"},{"CE1","HE1"},{"CE2","CZ"},{"CE2","HE2"},{"CZ","HZ"}}
	),
	PRO (
			2,
			new Atom[]{
					new Atom(Atom.Type.H, "HD2", new Point(0.26364338f, 2.48389f, 1.0090902f)),
					new Atom(Atom.Type.H, "HD3", new Point(0.63426304f, 3.1070147f, -0.6227222f)),
					new Atom(Atom.Type.H, "HB3", new Point(1.9562129f, -0.49511132f, -0.88778144f)),
					new Atom(Atom.Type.H, "HB2", new Point(1.9495054f, -0.4893711f, 0.8960867f)),
					new Atom(Atom.Type.O, "OXT", new Point(-1.2780714f, -1.2394655f, 0.4798122f)),
					new Atom(Atom.Type.C, "C", new Point(-0.5478119f, 0.21833281f, -1.4283396f)),
					new Atom(Atom.Type.H, "HA", new Point(-0.36621895f, 0.77802575f, 0.65928733f)),
					new Atom(Atom.Type.C, "CB", new Point(1.5596279f, 0.0f, -3.7252903E-8f)),
					new Atom(Atom.Type.N, "N", new Point(-0.33704197f, -1.3603464f, 0.58044225f)),
					new Atom(Atom.Type.O, "O", new Point(-0.92401654f, 1.336317f, -1.7372845f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.C, "CG", new Point(1.9267448f, 1.4640117f, -1.7881393E-7f)),
					new Atom(Atom.Type.C, "CD", new Point(0.5699285f, 2.2209892f, -3.5762787E-7f)),
					new Atom(Atom.Type.H, "HG3", new Point(2.4883904f, 1.7155303f, -0.89877653f)),
					new Atom(Atom.Type.H, "HG2", new Point(2.5207396f, 1.7220876f, 0.8833633f)),
			},
			new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD","CG"},{"CD","HD2"},{"CD","HD3"},{"CD","N"},{"CG","HG2"},{"CG","HG3"}}
	),
	SER (
			1,
			new Atom[]{
					new Atom(Atom.Type.H, "HG", new Point(2.2904675f, 1.7830939f, 1.5366819E-8f)),
					new Atom(Atom.Type.H, "HB3", new Point(1.9153792f, -0.23898019f, -0.893203f)),
					new Atom(Atom.Type.H, "HB2", new Point(1.9121757f, -0.64895093f, 0.65334976f)),
					new Atom(Atom.Type.O, "OG", new Point(2.0077262f, 1.170256f, 0.3243059f)),
					new Atom(Atom.Type.H, "HA", new Point(-0.3507117f, 0.37710112f, -0.09259632f)),
					new Atom(Atom.Type.C, "C", new Point(-0.5172005f, -0.36418873f, -0.4019931f)),
					new Atom(Atom.Type.N, "N", new Point(-0.4905777f, -0.14681509f, 0.51056874f)),
					new Atom(Atom.Type.C, "CB", new Point(1.5427651f, -2.9802322E-8f, -4.0978193E-8f)),
					new Atom(Atom.Type.O, "O", new Point(-0.8401468f, 0.0797241f, -0.3861181f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.H, "H", new Point(-0.085914075f, -0.4415348f, 0.9337152f)),
			},
			new String[][]{{"CA","CB"},{"CA","HA"},{"CB","HB2"},{"CB","HB3"},{"CB","OG"},{"HG","OG"}}
	),
	THR (
			1,
			new Atom[]{
					new Atom(Atom.Type.H, "HB", new Point(1.9097972f, -0.525153f, -0.87712824f)),
					new Atom(Atom.Type.H, "HG21", new Point(1.7367673f, 1.9899375f, 0.4081187f)),
					new Atom(Atom.Type.H, "HG22", new Point(2.1993313f, 1.8138108f, -0.6908644f)),
					new Atom(Atom.Type.C, "C", new Point(-0.5480514f, -0.48922974f, -0.21758896f)),
					new Atom(Atom.Type.H, "HA", new Point(-0.3448639f, 0.3211247f, -0.2241039f)),
					new Atom(Atom.Type.C, "CB", new Point(1.5506912f, 3.7252903E-9f, 1.8626451E-8f)),
					new Atom(Atom.Type.N, "N", new Point(-0.4714023f, 0.06494383f, 0.5034531f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.O, "O", new Point(-1.0765533f, -0.9079331f, -0.82306904f)),
					new Atom(Atom.Type.H, "H", new Point(0.1567296f, 0.13994145f, 0.6909087f)),
					new Atom(Atom.Type.C, "CG2", new Point(2.122128f, 1.4397808f, 7.0780516E-8f)),
					new Atom(Atom.Type.H, "HG23", new Point(2.8438044f, 1.5290201f, 0.28280637f)),
					new Atom(Atom.Type.O, "OG1", new Point(2.0060391f, -0.67720604f, 1.1564322f)),
					new Atom(Atom.Type.H, "HG1", new Point(2.6943424f, -0.6617912f, 1.2552574f)),
			},
			new String[][]{{"CA","HA"},{"CB","CA"},{"CB","CG2"},{"CB","HB"},{"CG2","HG21"},{"CG2","HG22"},{"HG23","CG2"},{"H","N"},{"OG1","CB"},{"OG1","HG1"}}
	),
	TRP (
			2,
			new Atom[]{
					new Atom(Atom.Type.H, "HB3", new Point(1.890697f, -0.5117537f, -0.88128805f)),
					new Atom(Atom.Type.H, "HD1", new Point(0.19107139f, 2.533909f, -0.007027626f)),
					new Atom(Atom.Type.H, "HB2", new Point(1.8906372f, -0.5103836f, 0.88109696f)),
					new Atom(Atom.Type.H, "HE1", new Point(1.7843378f, 4.5617967f, 9.949207E-4f)),
					new Atom(Atom.Type.N, "NE1", new Point(2.0915327f, 3.6324582f, 0.0019167662f)),
					new Atom(Atom.Type.H, "HE3", new Point(4.6796155f, 0.1273573f, 0.008452058f)),
					new Atom(Atom.Type.C, "C", new Point(-0.50185835f, -1.0078044f, -1.0365988f)),
					new Atom(Atom.Type.H, "HA", new Point(-0.3622539f, 0.9860605f, -0.24739283f)),
					new Atom(Atom.Type.C, "CE2", new Point(3.425757f, 3.284693f, 0.010258675f)),
					new Atom(Atom.Type.C, "CE3", new Point(4.6564536f, 1.2058139f, 0.013347626f)),
					new Atom(Atom.Type.C, "CZ3", new Point(5.858281f, 1.9344358f, 0.02355647f)),
					new Atom(Atom.Type.N, "N", new Point(-0.4932803f, -0.39219525f, 1.349632f)),
					new Atom(Atom.Type.C, "CB", new Point(1.5316856f, 0.0f, -1.1920929E-7f)),
					new Atom(Atom.Type.O, "O", new Point(-0.03469503f, -2.127325f, -1.1038381f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.H, "H", new Point(0.13728902f, -0.7197493f, 2.0190403f)),
					new Atom(Atom.Type.C, "CZ2", new Point(4.615203f, 4.013326f, 0.019627213f)),
					new Atom(Atom.Type.C, "CG", new Point(2.042766f, 1.4076043f, -2.9802322E-8f)),
					new Atom(Atom.Type.C, "CH2", new Point(5.8368163f, 3.3363156f, 0.0265373f)),
					new Atom(Atom.Type.H, "HZ2", new Point(4.591202f, 5.0937514f, 0.021454334f)),
					new Atom(Atom.Type.H, "HZ3", new Point(6.8023477f, 1.4114491f, 0.02926743f)),
					new Atom(Atom.Type.C, "CD2", new Point(3.422182f, 1.8707354f, 0.008342385f)),
					new Atom(Atom.Type.C, "CD1", new Point(1.2708465f, 2.5190399f, 2.3841858E-7f)),
					new Atom(Atom.Type.H, "HH2", new Point(6.7634773f, 3.8910403f, 0.03443885f)),
			},
			new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD1","CG"},{"CD1","HD1"},{"CD1","NE1"},{"CD2","CE2"},{"CD2","CE3"},{"CD2","CG"},{"CE2","CZ2"},{"CE2","NE1"},{"CE3","CZ3"},{"CE3","HE3"},{"CH2","CZ2"},{"CH2","CZ3"},{"CH2","HH2"},{"CZ2","HZ2"},{"CZ3","HZ3"},{"HE1","NE1"}}
	),
	TYR (
			2,
			new Atom[]{
					new Atom(Atom.Type.H, "HD2", new Point(4.2175794f, 0.9329951f, -0.030753374f)),
					new Atom(Atom.Type.C, "CZ", new Point(3.0180845f, 4.116975f, 0.0017871857f)),
					new Atom(Atom.Type.H, "HH", new Point(3.5764532f, 5.683056f, 0.92113006f)),
					new Atom(Atom.Type.H, "HB3", new Point(1.920023f, -0.4912784f, -0.90671694f)),
					new Atom(Atom.Type.H, "HD1", new Point(0.16037583f, 2.3399327f, -0.005210519f)),
					new Atom(Atom.Type.H, "HB2", new Point(1.9297255f, -0.54549533f, 0.8657804f)),
					new Atom(Atom.Type.H, "HE1", new Point(0.9461949f, 4.668454f, 0.003702283f)),
					new Atom(Atom.Type.H, "HE2", new Point(4.9921875f, 3.3002303f, -0.003575921f)),
					new Atom(Atom.Type.C, "C", new Point(-0.52145153f, 1.4172965f, -0.12747335f)),
					new Atom(Atom.Type.H, "HA", new Point(-0.35651812f, -0.38497528f, 0.9483938f)),
					new Atom(Atom.Type.C, "CE2", new Point(3.938054f, 3.0729573f, -0.006868005f)),
					new Atom(Atom.Type.N, "N", new Point(-0.538318f, -0.839745f, -1.0865648f)),
					new Atom(Atom.Type.C, "CB", new Point(1.5633687f, 0.0f, 1.1920929E-7f)),
					new Atom(Atom.Type.O, "O", new Point(-0.73651236f, 2.0729418f, 0.88809574f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.H, "H", new Point(0.05574748f, -1.2207314f, -1.7684348f)),
					new Atom(Atom.Type.C, "CG", new Point(2.1074343f, 1.4524292f, 3.7252903E-7f)),
					new Atom(Atom.Type.C, "CE1", new Point(1.6560732f, 3.8484652f, 0.002921939f)),
					new Atom(Atom.Type.O, "OH", new Point(3.4612784f, 5.4173775f, 0.008007169f)),
					new Atom(Atom.Type.C, "CD2", new Point(3.4966807f, 1.7372794f, -0.0063523054f)),
					new Atom(Atom.Type.C, "CD1", new Point(1.2035816f, 2.5277731f, 3.5762787E-7f)),
			},
			new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD1","CE1"},{"CD1","CG"},{"CD1","HD1"},{"CD2","CE2"},{"CD2","CG"},{"CD2","HD2"},{"CE1","CZ"},{"CE1","HE1"},{"CE2","CZ"},{"CE2","HE2"},{"CZ","OH"},{"HH","OH"}}
	),
	VAL (
			1,
			new Atom[]{
					new Atom(Atom.Type.H, "HB", new Point(1.9237461f, -0.49767262f, -0.9019165f)),
					new Atom(Atom.Type.H, "HG21", new Point(1.7537172f, -0.28819013f, 2.156619f)),
					new Atom(Atom.Type.H, "HG22", new Point(3.1937952f, -0.740852f, 1.2486148f)),
					new Atom(Atom.Type.C, "C", new Point(-0.548688f, 0.9920114f, 1.0378538f)),
					new Atom(Atom.Type.H, "HA", new Point(-0.3531378f, -0.9924094f, 0.26060772f)),
					new Atom(Atom.Type.H, "HG11", new Point(1.7568324f, 1.9868224f, 0.88781255f)),
					new Atom(Atom.Type.N, "N", new Point(-0.4841564f, 0.3476642f, -1.3381703f)),
					new Atom(Atom.Type.C, "CB", new Point(1.5663732f, 1.4901161E-8f, 2.9802322E-8f)),
					new Atom(Atom.Type.O, "O", new Point(-0.63978404f, 0.672876f, 2.2245915f)),
					new Atom(Atom.Type.C, "CA", new Point(0.0f, 0.0f, 0.0f)),
					new Atom(Atom.Type.H, "H", new Point(0.17288718f, 0.5738442f, -2.028506f)),
					new Atom(Atom.Type.C, "CG1", new Point(2.098382f, 1.4675925f, 1.7881393E-7f)),
					new Atom(Atom.Type.C, "CG2", new Point(2.1120448f, -0.75948286f, 1.2512137f)),
					new Atom(Atom.Type.H, "HG23", new Point(1.7851975f, -1.7891356f, 1.2319261f)),
					new Atom(Atom.Type.H, "HG13", new Point(3.1818562f, 1.4655569f, -0.017361283f)),
					new Atom(Atom.Type.H, "HG12", new Point(1.7355436f, 1.9903432f, -0.87856925f)),
			},
			new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG1"},{"CB","CG2"},{"CB","HB"},{"CG1","HG11"},{"CG1","HG12"},{"CG1","HG13"},{"CG2","HG21"},{"CG2","HG22"},{"CG2","HG23"}}
	);

	int chiAngleCount;
	Atom[] sidechainAtoms;
	String[][] atomBonds;

	List<String> followBond(String atomLabel) {
		Set<String> destinations = new HashSet<String>();
		for(String s[] : atomBonds) {
			if(s[0].equals(atomLabel)) {
				destinations.add(s[1]);
			}
			if(s[1].equals(atomLabel)) {
				destinations.add(s[0]);
			}
		}
		return new LinkedList<String>(destinations);
	}

	AminoAcidType(int chiN, Atom[] atoms, String[][] bonds) {
		sidechainAtoms = atoms;
		chiAngleCount = chiN;
		atomBonds = bonds;
	}
}
