package edu.j3dScene;
/*
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Vector;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Group;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.IndexedQuadArray;
import javax.media.j3d.Light;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.Material;
import javax.media.j3d.PointArray;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.PointLight;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Screen3D;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Switch;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Text3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.TriangleArray;
import javax.media.j3d.TriangleFanArray;
import javax.media.j3d.TriangleStripArray;
import javax.media.j3d.View;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Triangulator;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class AppearanceExplorer extends JApplet implements
    Java3DExplorerConstants {

  // Scene graph items
  SimpleUniverse u;

  Switch sceneSwitch;

  Group beethoven = null;

  Group galleon = null;

  Switch bgSwitch;

  IntChooser bgChooser;

  Appearance appearance;

  // image grabber
  boolean isApplication;

  Canvas3D canvas;

  OffScreenCanvas3D offScreenCanvas;

  View view;

  // ColoringAttributes
  ColoringAttributes coloringAttr;

  ColoringAttributesEditor coloringAttrEditor;

  Color3f coloringColor;

  int coloringShadeModel = ColoringAttributes.SHADE_GOURAUD;

  // PointAttributes
  PointAttributes pointAttr;

  PointAttributesEditor pointAttrEditor;

  float pointSize = 4.0f;

  boolean pointAAEnable = false;

  // LineAttributes
  LineAttributes lineAttr;

  float lineWidth = 1.0f;

  LineAttributesEditor lineAttrEditor;

  boolean lineAAEnable = false;

  int linePattern = LineAttributes.PATTERN_SOLID;

  // PolygonAttributes
  PolygonAttributes polygonAttr;

  PolygonAttributesEditor polygonAttrEditor;

  int polygonMode = PolygonAttributes.POLYGON_FILL;

  int polygonCull = PolygonAttributes.CULL_NONE;

  float polygonOffsetBias = 1.0f;

  float polygonOffsetFactor = 1.0f;

  // RenderingAttributes
  RenderingAttributes renderAttr;

  RenderingAttributesEditor renderAttrEditor;

  boolean renderVisible = true;

  boolean renderDepthBuffer = true;

  boolean renderDepthBufferWrite = true;

  boolean renderIgnoreVertexColor = false;

  boolean renderRasterOpEnable = false;

  int renderRasterOp = RenderingAttributes.ROP_COPY;

  // TransparencyAttributes
  TransparencyAttributes transpAttr;

  TransparencyAttributesEditor transpAttrEditor;

  int transpMode = TransparencyAttributes.NONE;

  float transpValue = 0.5f;

  // Material
  Material material;

  MaterialEditor materialEditor;

  // Texture2D
  Texture2DEditor texture2DEditor;

  boolean texEnable;

  String texImageFile;

  int texBoundaryModeS;

  int texBoundaryModeT;

  Color4f texBoundaryColor;

  int texMinFilter;

  int texMagFilter;

  int texMipMapMode;

  // TextureAttributes
  TextureAttributes textureAttr;

  TextureAttributesEditor textureAttrEditor;

  int texMode;

  Color4f texBlendColor;

  Transform3D texTransform;

  int texPerspCorrect;

  // TexCoordGeneration
  TexCoordGeneration texGen;

  TexCoordGenerationEditor texGenEditor;

  boolean texGenEnable;

  int texGenMode;

  Vector4f texGenPlaneS;

  Vector4f texGenPlaneT;

  // GUI helpers to allow galleon and beethoven to be loaded as needed
  // to reduce the startup time
  int galleonIndex;

  int beethovenIndex;

  String galleonString = "Obj File: Galleon";

  String beethovenString = "Obj File: Beethoven";

  BranchGroup beethovenPlaceholder;

  BranchGroup galleonPlaceholder;

  // Config items
  Switch lightSwitch;

  String snapImageString = "Snap Image";

  String outFileBase = "appear";

  int outFileSeq = 0;

  float offScreenScale = 1.5f;

  // Temporaries that are reused
  Transform3D tmpTrans = new Transform3D();

  Vector3f tmpVector = new Vector3f();

  AxisAngle4f tmpAxisAngle = new AxisAngle4f();

  // geometric constant
  Point3f origin = new Point3f();

  Vector3f yAxis = new Vector3f(0.0f, 1.0f, 0.0f);

  // NumberFormat to print out floats with only two digits
  NumberFormat nf;

  // Base for URLs, used to handle application/applet split
  String codeBaseString = null;

  // create the appearance and it's components
  void setupAppearance() {
    appearance = new Appearance();

    // ColoringAttributes
    coloringColor = new Color3f(red);
    coloringAttr = new ColoringAttributes(coloringColor, coloringShadeModel);
    coloringAttr.setCapability(ColoringAttributes.ALLOW_COLOR_WRITE);
    coloringAttr.setCapability(ColoringAttributes.ALLOW_SHADE_MODEL_WRITE);
    appearance.setColoringAttributes(coloringAttr);

    // set up the editor
    coloringAttrEditor = new ColoringAttributesEditor(coloringAttr);

    // PointAttributes
    pointAttr = new PointAttributes(pointSize, pointAAEnable);
    pointAttr.setCapability(PointAttributes.ALLOW_SIZE_WRITE);
    pointAttr.setCapability(PointAttributes.ALLOW_ANTIALIASING_WRITE);
    appearance.setPointAttributes(pointAttr);

    // set up the editor
    pointAttrEditor = new PointAttributesEditor(pointAttr);

    // LineAttributes
    lineAttr = new LineAttributes(lineWidth, linePattern, lineAAEnable);
    lineAttr.setCapability(LineAttributes.ALLOW_WIDTH_WRITE);
    lineAttr.setCapability(LineAttributes.ALLOW_PATTERN_WRITE);
    lineAttr.setCapability(LineAttributes.ALLOW_ANTIALIASING_WRITE);
    appearance.setLineAttributes(lineAttr);

    // set up the editor
    lineAttrEditor = new LineAttributesEditor(lineAttr);

    // PolygonAttributes
    polygonAttr = new PolygonAttributes(polygonMode, polygonCull, 0.0f);
    polygonAttr.setPolygonOffset(polygonOffsetBias);
    polygonAttr.setPolygonOffsetFactor(polygonOffsetFactor);
    polygonAttr.setCapability(PolygonAttributes.ALLOW_MODE_WRITE);
    polygonAttr.setCapability(PolygonAttributes.ALLOW_CULL_FACE_WRITE);
    polygonAttr.setCapability(PolygonAttributes.ALLOW_OFFSET_WRITE);
    appearance.setPolygonAttributes(polygonAttr);

    // set up the editor
    polygonAttrEditor = new PolygonAttributesEditor(polygonAttr);

    // Rendering attributes
    renderAttr = new RenderingAttributes(renderDepthBuffer,
        renderDepthBufferWrite, 0.0f, RenderingAttributes.ALWAYS,
        renderVisible, renderIgnoreVertexColor, renderRasterOpEnable,
        renderRasterOp);
    renderAttr
        .setCapability(RenderingAttributes.ALLOW_IGNORE_VERTEX_COLORS_WRITE);
    renderAttr.setCapability(RenderingAttributes.ALLOW_VISIBLE_WRITE);
    renderAttr.setCapability(RenderingAttributes.ALLOW_RASTER_OP_WRITE);
    renderAttr
        .setCapability(RenderingAttributes.ALLOW_ALPHA_TEST_FUNCTION_WRITE);
    renderAttr
        .setCapability(RenderingAttributes.ALLOW_ALPHA_TEST_VALUE_WRITE);
    appearance.setRenderingAttributes(renderAttr);
    appearance.setCapability(Appearance.ALLOW_RENDERING_ATTRIBUTES_WRITE);

    // set up the editor
    renderAttrEditor = new RenderingAttributesEditor(renderAttr);

    // TransparencyAttributes
    transpAttr = new TransparencyAttributes(transpMode, transpValue);
    transpAttr.setCapability(TransparencyAttributes.ALLOW_MODE_WRITE);
    transpAttr.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
    transpAttr
        .setCapability(TransparencyAttributes.ALLOW_BLEND_FUNCTION_WRITE);
    appearance.setTransparencyAttributes(transpAttr);

    // set up the editor
    transpAttrEditor = new TransparencyAttributesEditor(transpAttr);

    // Material
    material = new Material(red, black, red, white, 20.f);
    material.setLightingEnable(false);
    material.setCapability(Material.ALLOW_COMPONENT_WRITE);
    appearance.setMaterial(material);

    // material presets
    String[] materialNames = { "Red", "White", "Red Ambient",
        "Red Diffuse", "Grey Emissive", "White Specular", "Aluminium",
        "Blue Plastic", "Copper", "Gold", "Red Alloy", "Black Onyx" };
    Material[] materialPresets = new Material[materialNames.length];
    materialPresets[0] = new Material(red, black, red, white, 20.0f);
    materialPresets[1] = new Material(white, black, white, white, 20.0f);
    materialPresets[2] = new Material(red, black, black, black, 20.0f);
    materialPresets[3] = new Material(black, black, red, black, 20.0f);
    materialPresets[4] = new Material(black, grey, black, black, 20.0f);
    materialPresets[5] = new Material(black, black, black, white, 20.0f);
    Color3f alum = new Color3f(0.37f, 0.37f, 0.37f);
    Color3f alumSpec = new Color3f(0.89f, 0.89f, 0.89f);
    materialPresets[6] = new Material(alum, black, alum, alumSpec, 17);
    Color3f bluePlastic = new Color3f(0.20f, 0.20f, 0.70f);
    Color3f bluePlasticSpec = new Color3f(0.85f, 0.85f, 0.85f);
    materialPresets[7] = new Material(bluePlastic, black, bluePlastic,
        bluePlasticSpec, 22);
    Color3f copper = new Color3f(0.30f, 0.10f, 0.00f);
    ;
    Color3f copperSpec = new Color3f(0.75f, 0.30f, 0.00f);
    materialPresets[8] = new Material(copper, black, copper, copperSpec, 10);
    Color3f gold = new Color3f(0.49f, 0.34f, 0.00f);
    Color3f goldSpec = new Color3f(0.89f, 0.79f, 0.00f);
    materialPresets[9] = new Material(gold, black, gold, goldSpec, 15);
    Color3f redAlloy = new Color3f(0.34f, 0.00f, 0.34f);
    Color3f redAlloySpec = new Color3f(0.84f, 0.00f, 0.00f);
    materialPresets[10] = new Material(redAlloy, black, redAlloy,
        redAlloySpec, 15);
    Color3f blackOnyxSpec = new Color3f(0.72f, 0.72f, 0.72f);
    materialPresets[11] = new Material(black, black, black, blackOnyxSpec,
        23);

    // set up the editor
    materialEditor = new MaterialPresetEditor(material, materialNames,
        materialPresets);

    // Texture2D

    // set the values to the defaults
    texEnable = false;
    texMipMapMode = Texture.BASE_LEVEL;
    texBoundaryModeS = Texture.WRAP;
    texBoundaryModeT = Texture.WRAP;
    texMinFilter = Texture.BASE_LEVEL_POINT;
    texMagFilter = Texture.BASE_LEVEL_POINT;
    texBoundaryColor = new Color4f(0.0f, 0.0f, 0.0f, 0.0f);

    // set up the image choices
    String[] texImageNames = { "Earth", "Fish", };
    String[] texImageFileNames = { "earth.jpg", "fish1.gif", };
    int texImageFileIndex = 0;

    // set up the appearance to allow the texture to be changed
    appearance.setCapability(Appearance.ALLOW_TEXTURE_WRITE);

    // set up the editor (this will create the initial Texture2D and
    // assign it to the appearance)
    texture2DEditor = new Texture2DEditor(appearance, codeBaseString,
        texImageNames, texImageFileNames, texImageFileIndex, texEnable,
        texBoundaryModeS, texBoundaryModeT, texMinFilter, texMagFilter,
        texMipMapMode, texBoundaryColor);

    // TextureAttributes
    texMode = TextureAttributes.REPLACE;
    texBlendColor = new Color4f(1.0f, 1.0f, 1.0f, 1.0f);
    texTransform = new Transform3D();
    texPerspCorrect = TextureAttributes.NICEST;
    textureAttr = new TextureAttributes(texMode, texTransform,
        texBlendColor, texPerspCorrect);

    // set the capabilities to allow run time changes
    textureAttr.setCapability(TextureAttributes.ALLOW_MODE_WRITE);
    textureAttr.setCapability(TextureAttributes.ALLOW_BLEND_COLOR_WRITE);
    textureAttr.setCapability(TextureAttributes.ALLOW_TRANSFORM_WRITE);

    // connect it to the appearance
    appearance.setTextureAttributes(textureAttr);

    // setup the editor
    textureAttrEditor = new TextureAttributesEditor(textureAttr);

    // set up the tex coordinate generation
    texGenEnable = false;
    texGenMode = TexCoordGeneration.OBJECT_LINEAR;
    texGenPlaneS = new Vector4f(1.0f, 0.0f, 0.0f, 0.0f);
    texGenPlaneT = new Vector4f(0.0f, 1.0f, 0.0f, 0.0f);

    // set the appearance so that we can replace the tex gen when live
    appearance.setCapability(Appearance.ALLOW_TEXGEN_WRITE);

    // setup the editor
    texGenEditor = new TexCoordGenerationEditor(appearance, texGenEnable,
        texGenMode, texGenPlaneS, texGenPlaneT);

  }

  int powerOfTwo(int value) {
    int retval = 2;
    while (retval < value) {
      retval *= 2;
    }
    return retval;
  }

  // Point Array with three points
  Shape3D createPointArray() {

    Point3f pnt[] = new Point3f[3];
    pnt[0] = new Point3f(-1.0f, -1.0f, 0.0f);
    pnt[1] = new Point3f(1.0f, -1.0f, 0.0f);
    pnt[2] = new Point3f(1.0f, 1.0f, 0.0f);

    PointArray pa = new PointArray(3, GeometryArray.COORDINATES);
    pa.setCoordinates(0, pnt);

    return new Shape3D(pa, appearance);
  }

  // Line Array with two lines with vertex colors
  Shape3D createLineArray() {

    Point3f pnt[] = new Point3f[4];
    pnt[0] = new Point3f(-1.0f, -1.0f, 0.0f);
    pnt[1] = new Point3f(1.0f, -1.0f, 0.0f);
    pnt[2] = new Point3f(1.0f, 1.0f, 0.0f);
    pnt[3] = new Point3f(-1.0f, 1.0f, 0.0f);
    Color3f colrs[] = new Color3f[4];
    colrs[0] = black;
    colrs[1] = white;
    colrs[2] = red;
    colrs[3] = green;

    LineArray la = new LineArray(4, GeometryArray.COORDINATES
        | GeometryArray.COLOR_3);
    la.setCoordinates(0, pnt);
    la.setColors(0, colrs);

    return new Shape3D(la, appearance);
  }

  // Triangle Array with one triangle with vertex colors and a facet normal
  Shape3D createTriangleArray() {

    Point3f pnt[] = new Point3f[3];
    pnt[0] = new Point3f(-1.0f, -1.0f, 0.0f);
    pnt[1] = new Point3f(1.0f, -1.0f, 0.0f);
    pnt[2] = new Point3f(1.0f, 1.0f, 0.0f);
    Color3f colrs[] = new Color3f[3];
    colrs[0] = red;
    colrs[1] = green;
    colrs[2] = blue;
    Vector3f norms[] = new Vector3f[3];
    Vector3f triNormal = new Vector3f(0.0f, 0.0f, 1.0f);
    norms[0] = triNormal;
    norms[1] = triNormal;
    norms[2] = triNormal;

    TriangleArray ta = new TriangleArray(3, GeometryArray.COORDINATES
        | GeometryArray.COLOR_3 | GeometryArray.NORMALS);
    ta.setCoordinates(0, pnt);
    ta.setColors(0, colrs);
    ta.setNormals(0, norms);

    return new Shape3D(ta, appearance);
  }

  // Line Strip Array with two lines with 3 and 2 vertices each making
  // a two segment line and a one segment line
  Shape3D createLineStripArray() {

    int[] stripLengths = new int[2];
    stripLengths[0] = 3;
    stripLengths[1] = 2;
    Point3f pnt[] = new Point3f[5];
    // first line
    pnt[0] = new Point3f(-1.0f, -1.0f, 0.0f);
    pnt[1] = new Point3f(1.0f, -1.0f, 0.0f);
    pnt[2] = new Point3f(1.0f, 1.0f, 0.0f);
    // second line
    pnt[3] = new Point3f(0.5f, 0.5f, 0.0f);
    pnt[4] = new Point3f(-0.5f, -0.5f, 0.0f);

    LineStripArray lsa = new LineStripArray(5, GeometryArray.COORDINATES,
        stripLengths);
    lsa.setCoordinates(0, pnt);

    return new Shape3D(lsa, appearance);
  }

  Shape3D createTriangleStripArray() {

    int[] stripLengths = new int[1];
    stripLengths[0] = 5;
    Point3f pnt[] = new Point3f[5];
    pnt[0] = new Point3f(-1.0f, -1.0f, 0.0f);
    pnt[1] = new Point3f(1.0f, -1.0f, 0.0f);
    pnt[2] = new Point3f(-1.0f, 0.0f, 0.0f);
    pnt[3] = new Point3f(1.0f, 0.0f, 0.0f);
    pnt[4] = new Point3f(1.0f, 1.0f, 0.0f);

    TriangleStripArray tsa = new TriangleStripArray(5,
        GeometryArray.COORDINATES, stripLengths);
    tsa.setCoordinates(0, pnt);

    return new Shape3D(tsa, appearance);
  }

  Shape3D createTriangleFanArray() {

    int[] stripLengths = new int[1];
    stripLengths[0] = 5;
    Point3f pnt[] = new Point3f[5];
    pnt[0] = new Point3f(-1.0f, -1.0f, 0.0f);
    pnt[1] = new Point3f(1.0f, -1.0f, 0.0f);
    pnt[2] = new Point3f(1.0f, 0.0f, 0.0f);
    pnt[3] = new Point3f(0.0f, 1.0f, 0.0f);
    pnt[4] = new Point3f(-1.0f, 1.0f, 0.0f);

    TriangleFanArray tfa = new TriangleFanArray(5,
        GeometryArray.COORDINATES, stripLengths);
    tfa.setCoordinates(0, pnt);

    return new Shape3D(tfa, appearance);
  }

  Shape3D createTexTris() {

    Point3f pnt[] = new Point3f[9];
    pnt[0] = new Point3f(-0.8f, -0.8f, 0.0f);
    pnt[1] = new Point3f(-0.5f, -0.7f, 0.0f);
    pnt[2] = new Point3f(-0.7f, 0.7f, 0.0f);

    pnt[3] = new Point3f(-0.4f, 0.7f, 0.0f);
    pnt[4] = new Point3f(0.0f, -0.7f, 0.0f);
    pnt[5] = new Point3f(0.4f, 0.7f, 0.0f);

    pnt[6] = new Point3f(0.5f, 0.7f, 0.0f);
    pnt[7] = new Point3f(0.5f, -0.7f, 0.0f);
    pnt[8] = new Point3f(0.9f, 0.0f, 0.0f);

    TexCoord2f texCoord[] = new TexCoord2f[9];
    texCoord[0] = new TexCoord2f(0.05f, 0.90f);
    texCoord[1] = new TexCoord2f(0.25f, 0.10f);
    texCoord[2] = new TexCoord2f(1.00f, 0.60f);

    texCoord[3] = texCoord[0];
    texCoord[4] = texCoord[1];
    texCoord[5] = texCoord[2];

    texCoord[6] = texCoord[0];
    texCoord[7] = texCoord[1];
    texCoord[8] = texCoord[2];

    TriangleArray ta = new TriangleArray(9, GeometryArray.COORDINATES
        | GeometryArray.TEXTURE_COORDINATE_2);
    ta.setCoordinates(0, pnt);
    ta.setTextureCoordinates(0, 0, texCoord);

    return new Shape3D(ta, appearance);
  }

  Shape3D createTexSquare() {

    // color cube
    Point3f pnt[] = new Point3f[4];
    pnt[0] = new Point3f(-1.0f, -1.0f, 0.0f);
    pnt[1] = new Point3f(1.0f, -1.0f, 0.0f);
    pnt[2] = new Point3f(1.0f, 1.0f, 0.0f);
    pnt[3] = new Point3f(-1.0f, 1.0f, 0.0f);
    TexCoord2f texCoord[] = new TexCoord2f[4];
    texCoord[0] = new TexCoord2f(0.0f, 0.0f);
    texCoord[1] = new TexCoord2f(1.0f, 0.0f);
    texCoord[2] = new TexCoord2f(1.0f, 1.0f);
    texCoord[3] = new TexCoord2f(0.0f, 1.0f);

    QuadArray qa = new QuadArray(4, GeometryArray.COORDINATES
        | GeometryArray.TEXTURE_COORDINATE_2);
    qa.setCoordinates(0, pnt);
    qa.setTextureCoordinates(0, 0, texCoord);

    return new Shape3D(qa, appearance);
  }

  Shape3D createLargeTexSquare() {

    // color cube
    Point3f pnt[] = new Point3f[4];
    pnt[0] = new Point3f(-1.0f, -1.0f, 0.0f);
    pnt[1] = new Point3f(1.0f, -1.0f, 0.0f);
    pnt[2] = new Point3f(1.0f, 1.0f, 0.0f);
    pnt[3] = new Point3f(-1.0f, 1.0f, 0.0f);
    TexCoord2f texCoord[] = new TexCoord2f[4];
    texCoord[0] = new TexCoord2f(-1.0f, -1.0f);
    texCoord[1] = new TexCoord2f(2.0f, -1.0f);
    texCoord[2] = new TexCoord2f(2.0f, 2.0f);
    texCoord[3] = new TexCoord2f(-1.0f, 2.0f);

    QuadArray qa = new QuadArray(4, GeometryArray.COORDINATES
        | GeometryArray.TEXTURE_COORDINATE_2);
    qa.setCoordinates(0, pnt);
    qa.setTextureCoordinates(0, 0, texCoord);

    return new Shape3D(qa, appearance);
  }

  Shape3D createColorCube() {

    // color cube
    int[] indices = { 0, 3, 4, 2, // left face x = -1
        0, 1, 5, 3, // bottom face y = -1
        0, 2, 6, 1, // back face z = -1
        7, 5, 1, 6, // right face x = 1
        7, 6, 2, 4, // top face y = 1
        7, 4, 3, 5 // front face z = 1
    };

    Point3f pts[] = new Point3f[8];
    pts[0] = new Point3f(-1.0f, -1.0f, -1.0f);
    pts[1] = new Point3f(1.0f, -1.0f, -1.0f);
    pts[2] = new Point3f(-1.0f, 1.0f, -1.0f);
    pts[3] = new Point3f(-1.0f, -1.0f, 1.0f);
    pts[4] = new Point3f(-1.0f, 1.0f, 1.0f);
    pts[5] = new Point3f(1.0f, -1.0f, 1.0f);
    pts[6] = new Point3f(1.0f, 1.0f, -1.0f);
    pts[7] = new Point3f(1.0f, 1.0f, 1.0f);
    Color3f colr[] = new Color3f[8];
    colr[0] = black;
    colr[1] = red;
    colr[2] = green;
    colr[3] = blue;
    colr[4] = cyan;
    colr[5] = magenta;
    colr[6] = yellow;
    colr[7] = white;
    // The normals point out from 0,0,0, through the verticies of the
    // cube. These can be calculated by copying the coordinates to
    // a Vector3f and normalizing.
    Vector3f norm[] = new Vector3f[8];
    for (int i = 0; i < 8; i++) {
      norm[i] = new Vector3f(pts[i]);
      norm[i].normalize();
    }

    IndexedQuadArray iqa = new IndexedQuadArray(8,
        GeometryArray.COORDINATES | GeometryArray.COLOR_3
            | GeometryArray.NORMALS, 24);
    iqa.setCoordinates(0, pts);
    iqa.setColors(0, colr);
    iqa.setNormals(0, norm);
    iqa.setCoordinateIndices(0, indices);
    iqa.setColorIndices(0, indices);
    iqa.setNormalIndices(0, indices);
    return new Shape3D(iqa, appearance);
  }

  Shape3D createNGCube(float creaseAngle) {

    // color cube
    int[] indices = { 0, 3, 4, 2, // left face x = -1
        0, 1, 5, 3, // bottom face y = -1
        0, 2, 6, 1, // back face z = -1
        7, 5, 1, 6, // right face x = 1
        7, 6, 2, 4, // top face y = 1
        7, 4, 3, 5 // front face z = 1
    };

    Point3f pts[] = new Point3f[8];
    pts[0] = new Point3f(-1.0f, -1.0f, -1.0f);
    pts[1] = new Point3f(1.0f, -1.0f, -1.0f);
    pts[2] = new Point3f(-1.0f, 1.0f, -1.0f);
    pts[3] = new Point3f(-1.0f, -1.0f, 1.0f);
    pts[4] = new Point3f(-1.0f, 1.0f, 1.0f);
    pts[5] = new Point3f(1.0f, -1.0f, 1.0f);
    pts[6] = new Point3f(1.0f, 1.0f, -1.0f);
    pts[7] = new Point3f(1.0f, 1.0f, 1.0f);

    GeometryInfo gi = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
    gi.setCoordinates(pts);
    gi.setCoordinateIndices(indices);
    NormalGenerator ng = new NormalGenerator();
    ng.setCreaseAngle((float) Math.toRadians(creaseAngle));
    ng.generateNormals(gi);
    GeometryArray cube = gi.getGeometryArray();
    return new Shape3D(cube, appearance);
  }

  Shape3D createTriWithHole() {

    int[] stripCounts = new int[2];
    stripCounts[0] = 3;
    stripCounts[1] = 3;
    int[] contourCounts = new int[1];
    contourCounts[0] = 2;
    Point3f pnt[] = new Point3f[6];
    pnt[0] = new Point3f(-1.0f, -1.0f, 0.0f);
    pnt[1] = new Point3f(1.0f, -1.0f, 0.0f);
    pnt[2] = new Point3f(1.0f, 1.0f, 0.0f);
    pnt[3] = new Point3f(-0.6f, -0.8f, 0.0f);
    pnt[4] = new Point3f(0.8f, 0.6f, 0.0f);
    pnt[5] = new Point3f(0.8f, -0.8f, 0.0f);

    GeometryInfo gi = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
    gi.setCoordinates(pnt);
    gi.setStripCounts(stripCounts);
    gi.setContourCounts(contourCounts);

    Triangulator tr = new Triangulator();
    tr.triangulate(gi);
    GeometryArray triWithHole = gi.getGeometryArray();

    return new Shape3D(triWithHole, appearance);
  }

  Shape3D createText3D() {
    Font3D f3d = new Font3D(new Font(null, Font.PLAIN, 2),
        new FontExtrusion());
    Text3D t3d = new Text3D(f3d, "Text3D", new Point3f(-3.0f, -1.0f, 0.0f));
    Shape3D textShape = new Shape3D(t3d, appearance);
    return textShape;
  }

  BranchGroup createGalleon() {
    java.net.URL galleonURL = null;
    try {
      galleonURL = new java.net.URL(codeBaseString + "galleon.obj");
    } catch (Exception e) {
      System.err.println("Exception: " + e);
      System.exit(1);
    }

    int flags = ObjectFile.RESIZE;
    ObjectFile f = new ObjectFile(flags);
    Scene s = null;
    try {
      s = f.load(galleonURL);
    } catch (Exception e) {
      System.err.println(e);
      System.exit(1);
    }

    Group sceneGroup = s.getSceneGroup();

    Hashtable namedObjects = s.getNamedObjects();
    Enumeration e = namedObjects.keys();
    while (e.hasMoreElements()) {
      String name = (String) e.nextElement();
      //System.out.println("name = " + name);
      Shape3D shape = (Shape3D) namedObjects.get(name);
      shape.setAppearance(appearance);
    }

    BranchGroup retVal = new BranchGroup();
    retVal.addChild(s.getSceneGroup());
    return retVal;
  }

  BranchGroup createBeethoven() {
    java.net.URL beethovenURL = null;
    try {
      beethovenURL = new java.net.URL(codeBaseString + "beethoven.obj");
    } catch (Exception e) {
      System.err.println("Exception: " + e);
      System.exit(1);
    }

    int flags = ObjectFile.RESIZE;
    ObjectFile f = new ObjectFile(flags);
    Scene s = null;
    try {
      s = f.load(beethovenURL);
    } catch (Exception e) {
      System.err.println(e);
      System.exit(1);
    }

    Group sceneGroup = s.getSceneGroup();

    Hashtable namedObjects = s.getNamedObjects();
    Enumeration e = namedObjects.keys();
    while (e.hasMoreElements()) {
      String name = (String) e.nextElement();
      Shape3D shape = (Shape3D) namedObjects.get(name);
      shape.setAppearance(appearance);
    }

    BranchGroup retVal = new BranchGroup();
    retVal.addChild(s.getSceneGroup());
    return retVal;
  }

  // sets up the scene switch
  void setupSceneSwitch() {

    // create a Switch for the scene, allow switch changes
    sceneSwitch = new Switch();
    sceneSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
    sceneSwitch.setCapability(Switch.ALLOW_CHILDREN_READ);
    sceneSwitch.setCapability(Switch.ALLOW_CHILDREN_WRITE);
    sceneSwitch.setCapability(Switch.ALLOW_CHILDREN_EXTEND);

    Shape3D pointArray = createPointArray();
    sceneSwitch.addChild(pointArray);

    Shape3D lineArray = createLineArray();
    sceneSwitch.addChild(lineArray);

    Shape3D triangleArray = createTriangleArray();
    sceneSwitch.addChild(triangleArray);

    Shape3D lineStripArray = createLineStripArray();
    sceneSwitch.addChild(lineStripArray);

    Shape3D triangleStripArray = createTriangleStripArray();
    sceneSwitch.addChild(triangleStripArray);

    Shape3D triangleFanArray = createTriangleFanArray();
    sceneSwitch.addChild(triangleFanArray);

    Shape3D texTris = createTexTris();
    sceneSwitch.addChild(texTris);

    Shape3D texSquare = createTexSquare();
    sceneSwitch.addChild(texSquare);

    Shape3D largeTexSquare = createLargeTexSquare();
    sceneSwitch.addChild(largeTexSquare);

    Shape3D colorCube = createColorCube();
    sceneSwitch.addChild(colorCube);

    Shape3D ngCreaseCube = createNGCube(45);
    sceneSwitch.addChild(ngCreaseCube);

    Shape3D ngSmoothCube = createNGCube(100);
    sceneSwitch.addChild(ngSmoothCube);

    Shape3D triWithHole = createTriWithHole();
    sceneSwitch.addChild(triWithHole);

    // create a sphere with the shared appearance
    Sphere sphere = new Sphere(1.0f, Sphere.GENERATE_NORMALS
        | Sphere.GENERATE_TEXTURE_COORDS, appearance);
    sceneSwitch.addChild(sphere);

    // create a sphere with the shared appearance
    Sphere lrSphere = new Sphere(1.0f, Sphere.GENERATE_NORMALS
        | Sphere.GENERATE_TEXTURE_COORDS, 10, appearance);
    sceneSwitch.addChild(lrSphere);

    // create a sphere with the shared appearance
    Sphere hrSphere = new Sphere(1.0f, Sphere.GENERATE_NORMALS
        | Sphere.GENERATE_TEXTURE_COORDS, 45, appearance);
    sceneSwitch.addChild(hrSphere);

    // Text3D
    Shape3D text3D = createText3D();
    sceneSwitch.addChild(text3D);

    // galleon -- use a placeholder to indicate it hasn't been loaded yet
    // then load it the first time it gets asked for
    //was:
    //Group galleon = createGalleon();
    //sceneSwitch.addChild(galleon);
    galleonIndex = sceneSwitch.numChildren();
    galleonPlaceholder = new BranchGroup();
    galleonPlaceholder.setCapability(BranchGroup.ALLOW_DETACH);
    sceneSwitch.addChild(galleonPlaceholder);

    // beethoven -- use a placeholder to indicate it hasn't been loaded yet
    // then load it the first time it gets asked for
    //was:
    //Group beethoven = createBeethoven();
    //sceneSwitch.addChild(beethoven);
    beethovenIndex = sceneSwitch.numChildren();
    beethovenPlaceholder = new BranchGroup();
    beethovenPlaceholder.setCapability(BranchGroup.ALLOW_DETACH);
    sceneSwitch.addChild(beethovenPlaceholder);
  }

   //
   //Set up the lights. This is a group which contains the ambient light and a
   //switch for the other lights. directional : white light pointing along Z
   //axis point : white light near upper left corner of spheres spot : white
   //light near upper left corner of spheres, pointing towards center.
   //
  Group setupLights() {

    Group group = new Group();

    // set up the BoundingSphere for all the lights
    BoundingSphere bounds = new BoundingSphere(new Point3d(), 100.0);

    // Set up the ambient light
    AmbientLight lightAmbient = new AmbientLight(medGrey);
    lightAmbient.setInfluencingBounds(bounds);
    lightAmbient.setCapability(Light.ALLOW_STATE_WRITE);
    group.addChild(lightAmbient);

    lightSwitch = new Switch();
    lightSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
    group.addChild(lightSwitch);

    // Set up the directional light
    Vector3f lightDirection1 = new Vector3f(0.0f, 0.0f, -1.0f);
    DirectionalLight lightDirectional1 = new DirectionalLight(white,
        lightDirection1);
    lightDirectional1.setInfluencingBounds(bounds);
    lightDirectional1.setCapability(Light.ALLOW_STATE_WRITE);
    lightSwitch.addChild(lightDirectional1);

    Point3f lightPos1 = new Point3f(-4.0f, 8.0f, 16.0f);
    Point3f lightAttenuation1 = new Point3f(1.0f, 0.0f, 0.0f);
    PointLight pointLight1 = new PointLight(brightWhite, lightPos1,
        lightAttenuation1);
    pointLight1.setInfluencingBounds(bounds);
    lightSwitch.addChild(pointLight1);

    Point3f lightPos2 = new Point3f(-16.0f, 8.0f, 4.0f);
    //Point3f lightPos = new Point3f(-4.0f, 2.0f, 1.0f);
    Point3f lightAttenuation2 = new Point3f(1.0f, 0.0f, 0.0f);
    PointLight pointLight2 = new PointLight(white, lightPos2,
        lightAttenuation2);
    pointLight2.setInfluencingBounds(bounds);
    lightSwitch.addChild(pointLight2);

    return group;
  }

  BranchGroup createSceneGraph() {
    // Create the root of the branch graph
    BranchGroup objRoot = new BranchGroup();

    // Add the primitives to the scene
    setupAppearance();
    setupSceneSwitch();
    objRoot.addChild(sceneSwitch);
    objRoot.addChild(bgSwitch);
    Group lightGroup = setupLights();
    objRoot.addChild(lightGroup);

    return objRoot;
  }

  public AppearanceExplorer() {
    this(false, 1.0f);
  }

  public AppearanceExplorer(boolean isApplication, float initOffScreenScale) {
    this.isApplication = isApplication;
    this.offScreenScale = initOffScreenScale;
  }

  public void init() {

    // initialize the code base
    try {
      java.net.URL codeBase = getCodeBase();
      codeBaseString = codeBase.toString();
    } catch (Exception e) {
      // probably running as an application, try the application
      // code base
      codeBaseString = "file:./";
    }

    // set up a NumFormat object to print out float with only 3 fraction
    // digits
    nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(3);

    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    GraphicsConfiguration config = SimpleUniverse
        .getPreferredConfiguration();

    canvas = new Canvas3D(config);
    canvas.setSize(600, 600);

    u = new SimpleUniverse(canvas);

    if (isApplication) {
      offScreenCanvas = new OffScreenCanvas3D(config, true);
      // set the size of the off-screen canvas based on a scale
      // of the on-screen size
      Screen3D sOn = canvas.getScreen3D();
      Screen3D sOff = offScreenCanvas.getScreen3D();
      Dimension dim = sOn.getSize();
      dim.width *= offScreenScale;
      dim.height *= offScreenScale;
      sOff.setSize(dim);
      sOff.setPhysicalScreenWidth(sOn.getPhysicalScreenWidth()
          * offScreenScale);
      sOff.setPhysicalScreenHeight(sOn.getPhysicalScreenHeight()
          * offScreenScale);

      // attach the offscreen canvas to the view
      u.getViewer().getView().addCanvas3D(offScreenCanvas);
    }
    contentPane.add("Center", canvas);

    BackgroundTool bgTool = new BackgroundTool(codeBaseString);
    bgSwitch = bgTool.getSwitch();
    bgChooser = bgTool.getChooser();

    // Create a simple scene and attach it to the virtual universe
    BranchGroup scene = createSceneGraph();

    // set up sound
    u.getViewer().createAudioDevice();

    // get the view
    view = u.getViewer().getView();

    // Get the viewing platform
    ViewingPlatform viewingPlatform = u.getViewingPlatform();

    // Move the viewing platform back to enclose the -2 -> 2 range
    double viewRadius = 2.0; // want to be able to see circle
    // of viewRadius size around origin
    // get the field of view
    double fov = u.getViewer().getView().getFieldOfView();

    // calc view distance to make circle view in fov
    float viewDistance = (float) (viewRadius / Math.tan(fov / 2.0));
    tmpVector.set(0.0f, 0.0f, viewDistance);// setup offset
    tmpTrans.set(tmpVector); // set trans to translate
    // move the view platform
    viewingPlatform.getViewPlatformTransform().setTransform(tmpTrans);

    // add an orbit behavior to move the viewing platform
    OrbitBehavior orbit = new OrbitBehavior(canvas,
        OrbitBehavior.PROPORTIONAL_ZOOM | OrbitBehavior.REVERSE_ROTATE
            | OrbitBehavior.REVERSE_TRANSLATE);
    orbit.setZoomFactor(0.25);
    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
        100.0);
    orbit.setSchedulingBounds(bounds);    viewingPlatform.setViewPlatformBehavior(orbit);

    u.addBranchGraph(scene);

    contentPane.add("East", guiPanel());
  }

  public void destroy() {
    u.removeAllLocales();
  }

  // create a panel with a tabbed pane holding each of the edit panels
  JPanel guiPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab("Setup", setupPanel());
    tabbedPane.addTab("ColoringAttributes", coloringAttrEditor);
    tabbedPane.addTab("PointAttributes", pointAttrEditor);
    tabbedPane.addTab("LineAttributes", lineAttrEditor);
    tabbedPane.addTab("PolygonAttributes", polygonAttrEditor);
    tabbedPane.addTab("RenderingAttributes", renderAttrEditor);
    tabbedPane.addTab("TransparencyAttributes", transpAttrEditor);
    tabbedPane.addTab("Material", materialEditor);
    tabbedPane.addTab("Texture2D", texture2DEditor);
    tabbedPane.addTab("TextureAttributes", textureAttrEditor);
    tabbedPane.addTab("TexCoordGeneration", texGenEditor);
    panel.add("Center", tabbedPane);

    return panel;
  }

  JPanel setupPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(0, 1));

    // This order of the names must match the cases in the Switch in
    // setupSceneSwitch
    String[] dataNames = { "Point Array", "Line Array", "Triangle Array",
        "Line Strip Array", "Triangle Strip Array",
        "Triangle Fan Array", "Textured Triangles", "Textured Square",
        "Large Texture Square", "Color Cube", "Norm Gen Cube - Crease",
        "Norm Gen Cube - Smooth", "Tri with hole", "Sphere",
        "Low-res Sphere", "High-res Sphere", "Text 3D", galleonString,
        beethovenString, };

    IntChooser dataChooser = new IntChooser("Data:", dataNames);
    dataChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        int value = event.getValue();
        if (sceneSwitch.getChild(value) == beethovenPlaceholder) {
          beethoven = createBeethoven();
          sceneSwitch.setChild(beethoven, beethovenIndex);
        } else if (sceneSwitch.getChild(value) == galleonPlaceholder) {
          galleon = createGalleon();
          sceneSwitch.setChild(galleon, galleonIndex);
        }
        sceneSwitch.setWhichChild(value);
      }
    });
    dataChooser.setValueByName("Sphere");

    panel.add(dataChooser);

    panel.add(bgChooser);

    String[] lightNames = { "Ambient Only", "Directional", "Point Light 1",
        "Point Light 2", };
    int[] lightValues = { Switch.CHILD_NONE, 0, 1, 2 };

    IntChooser lightChooser = new IntChooser("Light:", lightNames,
        lightValues, 0);
    lightChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        int value = event.getValue();
        lightSwitch.setWhichChild(value);
      }
    });
    lightChooser.setValueByName("Point Light 1");

    panel.add(lightChooser);

    panel.add(new JLabel(""));

    if (isApplication) {
      JButton snapButton = new JButton(snapImageString);
      snapButton.setActionCommand(snapImageString);
      snapButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          doSnapshot();
        }
      });
      panel.add(snapButton);
    }

    return panel;
  }

  void doSnapshot() {
    Point loc = canvas.getLocationOnScreen();
    offScreenCanvas.setOffScreenLocation(loc);
    Dimension dim = canvas.getSize();
    dim.width *= offScreenScale;
    dim.height *= offScreenScale;
    nf.setMinimumIntegerDigits(3);
    offScreenCanvas.snapImageFile(outFileBase + nf.format(outFileSeq++),
        dim.width, dim.height);
    nf.setMinimumIntegerDigits(0);
  }

  // The following allows AppearanceExplorer to be run as an application
  // as well as an applet
  //
  public static void main(String[] args) {
    float initOffScreenScale = 2.5f;
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-s")) {
        if (args.length >= (i + 1)) {
          initOffScreenScale = Float.parseFloat(args[i + 1]);
          i++;
        }
      }
    }
    new MainFrame(new AppearanceExplorer(true, initOffScreenScale), 950,
        600);
  }
}

interface IntListener extends EventListener {
  void intChanged(IntEvent e);
}

class IntEvent extends EventObject {

  int value;

  IntEvent(Object source, int newValue) {
    super(source);
    value = newValue;
  }

  int getValue() {
    return value;
  }
}

interface Java3DExplorerConstants {

  // colors
  static Color3f black = new Color3f(0.0f, 0.0f, 0.0f);

  static Color3f red = new Color3f(1.0f, 0.0f, 0.0f);

  static Color3f green = new Color3f(0.0f, 1.0f, 0.0f);

  static Color3f blue = new Color3f(0.0f, 0.0f, 1.0f);

  static Color3f skyBlue = new Color3f(0.6f, 0.7f, 0.9f);

  static Color3f cyan = new Color3f(0.0f, 1.0f, 1.0f);

  static Color3f magenta = new Color3f(1.0f, 0.0f, 1.0f);

  static Color3f yellow = new Color3f(1.0f, 1.0f, 0.0f);

  static Color3f brightWhite = new Color3f(1.0f, 1.5f, 1.5f);

  static Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

  static Color3f darkGrey = new Color3f(0.15f, 0.15f, 0.15f);

  static Color3f medGrey = new Color3f(0.3f, 0.3f, 0.3f);

  static Color3f grey = new Color3f(0.5f, 0.5f, 0.5f);

  static Color3f lightGrey = new Color3f(0.75f, 0.75f, 0.75f);

  // infinite bounding region, used to make env nodes active everywhere
  BoundingSphere infiniteBounds = new BoundingSphere(new Point3d(),
      Double.MAX_VALUE);

  // common values
  static final String nicestString = "NICEST";

  static final String fastestString = "FASTEST";

  static final String antiAliasString = "Anti-Aliasing";

  static final String noneString = "NONE";

  // light type constants
  static int LIGHT_AMBIENT = 1;

  static int LIGHT_DIRECTIONAL = 2;

  static int LIGHT_POSITIONAL = 3;

  static int LIGHT_SPOT = 4;

  // screen capture constants
  static final int USE_COLOR = 1;

  static final int USE_BLACK_AND_WHITE = 2;

  // number formatter
  NumberFormat nf = NumberFormat.getInstance();

}

class IntChooser extends JPanel implements Java3DExplorerConstants {

  JComboBox combo;

  String[] choiceNames;

  int[] choiceValues;

  int current;

  Vector listeners = new Vector();

  IntChooser(String name, String[] initChoiceNames, int[] initChoiceValues,
      int initValue) {
    if ((initChoiceValues != null)
        && (initChoiceNames.length != initChoiceValues.length)) {
      throw new IllegalArgumentException(
          "Name and Value arrays must have the same length");
    }
    choiceNames = new String[initChoiceNames.length];
    choiceValues = new int[initChoiceNames.length];
    System
        .arraycopy(initChoiceNames, 0, choiceNames, 0,
            choiceNames.length);
    if (initChoiceValues != null) {
      System.arraycopy(initChoiceValues, 0, choiceValues, 0,
          choiceNames.length);
    } else {
      for (int i = 0; i < initChoiceNames.length; i++) {
        choiceValues[i] = i;
      }
    }

    // Create the combo box, select the init value
    combo = new JComboBox(choiceNames);
    combo.setSelectedIndex(current);
    combo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        int index = cb.getSelectedIndex();
        setValueIndex(index);
      }
    });

    // set the initial value
    current = 0;
    setValue(initValue);

    // layout to align left
    setLayout(new BorderLayout());
    Box box = new Box(BoxLayout.X_AXIS);
    add(box, BorderLayout.WEST);

    box.add(new JLabel(name));
    box.add(combo);
  }

  IntChooser(String name, String[] initChoiceNames, int[] initChoiceValues) {
    this(name, initChoiceNames, initChoiceValues, initChoiceValues[0]);
  }

  IntChooser(String name, String[] initChoiceNames, int initValue) {
    this(name, initChoiceNames, null, initValue);
  }

  IntChooser(String name, String[] initChoiceNames) {
    this(name, initChoiceNames, null, 0);
  }

  public void addIntListener(IntListener listener) {
    listeners.add(listener);
  }

  public void removeIntListener(IntListener listener) {
    listeners.remove(listener);
  }

  public void setValueByName(String newName) {
    boolean found = false;
    int newIndex = 0;
    for (int i = 0; (!found) && (i < choiceNames.length); i++) {
      if (newName.equals(choiceNames[i])) {
        newIndex = i;
        found = true;
      }
    }
    if (found) {
      setValueIndex(newIndex);
    }
  }

  public void setValue(int newValue) {
    boolean found = false;
    int newIndex = 0;
    for (int i = 0; (!found) && (i < choiceValues.length); i++) {
      if (newValue == choiceValues[i]) {
        newIndex = i;
        found = true;
      }
    }
    if (found) {
      setValueIndex(newIndex);
    }
  }

  public int getValue() {
    return choiceValues[current];
  }

  public String getValueName() {
    return choiceNames[current];
  }

  public void setValueIndex(int newIndex) {
    boolean changed = (newIndex != current);
    current = newIndex;
    if (changed) {
      combo.setSelectedIndex(current);
      valueChanged();
    }
  }

  private void valueChanged() {
    // notify the listeners
    IntEvent event = new IntEvent(this, choiceValues[current]);
    for (Enumeration e = listeners.elements(); e.hasMoreElements();) {
      IntListener listener = (IntListener) e.nextElement();
      listener.intChanged(event);
    }
  }

}

class OffScreenCanvas3D extends Canvas3D {

  OffScreenCanvas3D(GraphicsConfiguration graphicsConfiguration,
      boolean offScreen) {

    super(graphicsConfiguration, offScreen);
  }

  private BufferedImage doRender(int width, int height) {

    BufferedImage bImage = new BufferedImage(width, height,
        BufferedImage.TYPE_INT_RGB);

    ImageComponent2D buffer = new ImageComponent2D(
        ImageComponent.FORMAT_RGB, bImage);
    //buffer.setYUp(true);

    setOffScreenBuffer(buffer);
    renderOffScreenBuffer();
    waitForOffScreenRendering();
    bImage = getOffScreenBuffer().getImage();
    return bImage;
  }

  void snapImageFile(String filename, int width, int height) {
    BufferedImage bImage = doRender(width, height);

    //
    // JAI: RenderedImage fImage = JAI.create("format", bImage,
    // DataBuffer.TYPE_BYTE); JAI.create("filestore", fImage, filename +
    // ".tif", "tiff", null);
    ///

    // No JAI: 
    try {
      FileOutputStream fos = new FileOutputStream(filename + ".jpg");
      BufferedOutputStream bos = new BufferedOutputStream(fos);

      JPEGImageEncoder jie = JPEGCodec.createJPEGEncoder(bos);
      JPEGEncodeParam param = jie.getDefaultJPEGEncodeParam(bImage);
      param.setQuality(1.0f, true);
      jie.setJPEGEncodeParam(param);
      jie.encode(bImage);

      bos.flush();
      fos.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}

class ColoringAttributesEditor extends Box implements Java3DExplorerConstants {

  ColoringAttributes coloringAttr;

  Color3f color = new Color3f();

  int coloringShadeModel;

  public ColoringAttributesEditor(ColoringAttributes init) {
    super(BoxLayout.Y_AXIS);
    coloringAttr = init;
    coloringAttr.getColor(color);
    coloringShadeModel = coloringAttr.getShadeModel();

    String[] shadeNames = { "SHADE_FLAT", "SHADE_GOURAUD", "NICEST",
        "FASTEST" };
    int[] shadeValues = { ColoringAttributes.SHADE_FLAT,
        ColoringAttributes.SHADE_GOURAUD, ColoringAttributes.NICEST,
        ColoringAttributes.FASTEST };

    IntChooser shadeChooser = new IntChooser("Shade model:", shadeNames,
        shadeValues, coloringShadeModel);
    shadeChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        int value = event.getValue();
        coloringAttr.setShadeModel(value);
      }
    });
    add(shadeChooser);

    Color3fEditor colorEditor = new Color3fEditor("Color", color);
    colorEditor.addColor3fListener(new Color3fListener() {
      public void colorChanged(Color3fEvent event) {
        event.getValue(color);
        coloringAttr.setColor(color);
      }
    });
    add(colorEditor);
  }

}

class Color3fEditor extends JPanel implements ActionListener,
    Java3DExplorerConstants {

  String name;

  Color3f color = new Color3f();

  JButton button;

  JPanel preview;

  Vector listeners = new Vector();

  public Color3fEditor(String initName, Color3f initColor) {
    name = initName;
    color.set(initColor);

    JLabel label = new JLabel(name);

    preview = new JPanel();
    preview.setPreferredSize(new Dimension(40, 40));
    preview.setBackground(color.get());
    preview.setBorder(BorderFactory.createRaisedBevelBorder());

    button = new JButton("Set");
    button.addActionListener(this);

    JPanel filler = new JPanel();
    filler.setPreferredSize(new Dimension(100, 20));

    setLayout(new BorderLayout());
    Box box = new Box(BoxLayout.X_AXIS);
    add(box, BorderLayout.WEST);

    box.add(label);
    box.add(preview);
    box.add(button);
    box.add(filler);

  }

  public void actionPerformed(ActionEvent e) {
    Color currentColor = color.get();
    Color newColor = JColorChooser.showDialog(this, name, currentColor);
    if (newColor != null) {
      color.set(newColor);
      valueChanged();
    }
  }

  public void setValue(Color3f newValue) {
    boolean changed = !color.equals(newValue);
    if (changed) {
      color.set(newValue);
      valueChanged();
    }
  }

  public void addColor3fListener(Color3fListener listener) {
    listeners.add(listener);
  }

  public void removeColor3fListener(Color3fListener listener) {
    listeners.remove(listener);
  }

  private void valueChanged() {
    // update the preview
    preview.setBackground(color.get());

    // notify the listeners
    Color3fEvent event = new Color3fEvent(this, color);
    for (Enumeration e = listeners.elements(); e.hasMoreElements();) {
      Color3fListener listener = (Color3fListener) e.nextElement();
      listener.colorChanged(event);
    }
  }
}

class Color3fEvent extends EventObject {

  Color3f value = new Color3f();

  Color3fEvent(Object source, Color3f newValue) {
    super(source);
    value.set(newValue);
  }

  void getValue(Color3f getValue) {
    getValue.set(value);
  }
}

interface Color3fListener extends EventListener {
  void colorChanged(Color3fEvent e);
}

class PointAttributesEditor extends Box implements Java3DExplorerConstants {

  // PointAttributes
  PointAttributes pointAttr;

  float pointSize;

  boolean pointAAEnable;

  String pointAAString = "Point AA";

  PointAttributesEditor(PointAttributes init) {
    super(BoxLayout.Y_AXIS);
    pointAttr = init;
    pointSize = pointAttr.getPointSize();
    pointAAEnable = pointAttr.getPointAntialiasingEnable();

    LogFloatLabelJSlider pointSizeSlider = new LogFloatLabelJSlider("Size",
        0.1f, 100.0f, pointSize);
    pointSizeSlider.setMajorTickSpacing(1.0f);
    pointSizeSlider.setPaintTicks(true);
    pointSizeSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        pointSize = e.getValue();
        pointAttr.setPointSize(pointSize);
      }
    });
    add(pointSizeSlider);

    JCheckBox pointAACheckBox = new JCheckBox(antiAliasString);
    pointAACheckBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JCheckBox checkbox = (JCheckBox) e.getSource();
        pointAAEnable = checkbox.isSelected();
        pointAttr.setPointAntialiasingEnable(pointAAEnable);
      }
    });

    add(new LeftAlignComponent(pointAACheckBox));
  }
}

class LineAttributesEditor extends Box implements Java3DExplorerConstants {

  // LineAttributes
  LineAttributes lineAttr;

  float lineWidth;

  int linePattern;

  boolean lineAAEnable;

  LineAttributesEditor(LineAttributes init) {
    super(BoxLayout.Y_AXIS);
    lineAttr = init;
    lineWidth = lineAttr.getLineWidth();
    linePattern = lineAttr.getLinePattern();
    lineAAEnable = lineAttr.getLineAntialiasingEnable();

    FloatLabelJSlider lineWidthSlider = new FloatLabelJSlider("Width",
        0.1f, 0.0f, 5.0f, lineWidth);
    lineWidthSlider.setMajorTickSpacing(1.0f);
    lineWidthSlider.setPaintTicks(true);
    lineWidthSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        lineWidth = e.getValue();
        lineAttr.setLineWidth(lineWidth);
      }
    });
    lineWidthSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
    add(lineWidthSlider);

    String[] patternNames = { "PATTERN_SOLID", "PATTERN_DASH",
        "PATTERN_DOT", "PATTERN_DASH_DOT" };
    int[] patternValues = { LineAttributes.PATTERN_SOLID,
        LineAttributes.PATTERN_DASH, LineAttributes.PATTERN_DOT,
        LineAttributes.PATTERN_DASH_DOT };

    IntChooser patternChooser = new IntChooser("Pattern:", patternNames,
        patternValues, linePattern);
    patternChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        int value = event.getValue();
        lineAttr.setLinePattern(value);
      }
    });
    patternChooser.setAlignmentX(Component.LEFT_ALIGNMENT);
    add(patternChooser);

    JCheckBox lineAACheckBox = new JCheckBox(antiAliasString);
    lineAACheckBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JCheckBox checkbox = (JCheckBox) e.getSource();
        lineAAEnable = checkbox.isSelected();
        lineAttr.setLineAntialiasingEnable(lineAAEnable);
      }
    });
    lineAACheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
    // add the checkbox to the panel
    add(lineAACheckBox);
  }
}

class PolygonAttributesEditor extends Box implements Java3DExplorerConstants {

  // PolygonAttributes
  PolygonAttributes polygonAttr;

  int polygonMode;

  int cullFace;

  float polygonOffset;

  float polygonOffsetFactor;

  boolean backFaceNormalFlip;

  PolygonAttributesEditor(PolygonAttributes init) {
    super(BoxLayout.Y_AXIS);
    polygonAttr = init;
    polygonMode = polygonAttr.getPolygonMode();
    cullFace = polygonAttr.getCullFace();
    polygonOffset = polygonAttr.getPolygonOffset();
    polygonOffsetFactor = polygonAttr.getPolygonOffsetFactor();
    backFaceNormalFlip = polygonAttr.getBackFaceNormalFlip();

    String[] modeNames = { "POLYGON_POINT", "POLYGON_LINE", "POLYGON_FILL", };
    int[] modeValues = { PolygonAttributes.POLYGON_POINT,
        PolygonAttributes.POLYGON_LINE, PolygonAttributes.POLYGON_FILL, };
    IntChooser modeChooser = new IntChooser("Mode:", modeNames, modeValues,
        polygonMode);
    modeChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        polygonMode = event.getValue();
        polygonAttr.setPolygonMode(polygonMode);
      }
    });
    add(modeChooser);

    String[] cullNames = { "CULL_NONE", "CULL_BACK", "CULL_FRONT", };
    int[] cullValues = { PolygonAttributes.CULL_NONE,
        PolygonAttributes.CULL_BACK, PolygonAttributes.CULL_FRONT, };
    IntChooser cullChooser = new IntChooser("Cull:", cullNames, cullValues,
        cullFace);
    cullChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        cullFace = event.getValue();
        polygonAttr.setCullFace(cullFace);
      }
    });
    add(cullChooser);

    FloatLabelJSlider polygonOffsetSlider = new FloatLabelJSlider("Offset",
        0.1f, 0.0f, 2.0f, polygonOffset);
    polygonOffsetSlider.setMajorTickSpacing(1.0f);
    polygonOffsetSlider.setPaintTicks(true);
    polygonOffsetSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        polygonOffset = e.getValue();
        polygonAttr.setPolygonOffset(polygonOffset);
      }
    });
    add(polygonOffsetSlider);

    LogFloatLabelJSlider polygonOffsetFactorSlider = new LogFloatLabelJSlider(
        "Offset Factor", 0.1f, 10000.0f, polygonOffsetFactor);
    polygonOffsetFactorSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        polygonOffsetFactor = e.getValue();
        polygonAttr.setPolygonOffsetFactor(polygonOffsetFactor);
      }
    });
    add(polygonOffsetFactorSlider);

    JCheckBox backFaceNormalFlipCheckBox = new JCheckBox(
        "BackFaceNormalFlip", backFaceNormalFlip);
    backFaceNormalFlipCheckBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JCheckBox checkbox = (JCheckBox) e.getSource();
        backFaceNormalFlip = checkbox.isSelected();
        polygonAttr.setBackFaceNormalFlip(backFaceNormalFlip);
      }
    });
    // no ablity to change without replcing polygon attributes
    backFaceNormalFlipCheckBox.setEnabled(false);

    add(new LeftAlignComponent(backFaceNormalFlipCheckBox));
  }
}

class RenderingAttributesEditor extends JPanel implements
    Java3DExplorerConstants {

  // RenderingAttributes
  RenderingAttributes renderingAttr;

  boolean visible;

  boolean depthBufferEnable;

  boolean depthBufferWriteEnable;

  boolean ignoreVertexColors;

  boolean rasterOpEnable;

  int rasterOp;

  int alphaTestFunction;

  float alphaTestValue;

  RenderingAttributesEditor(RenderingAttributes init) {
    renderingAttr = init;
    visible = renderingAttr.getVisible();
    depthBufferEnable = renderingAttr.getDepthBufferEnable();
    depthBufferWriteEnable = renderingAttr.getDepthBufferWriteEnable();
    ignoreVertexColors = renderingAttr.getIgnoreVertexColors();
    rasterOpEnable = renderingAttr.getRasterOpEnable();
    rasterOp = renderingAttr.getRasterOp();
    alphaTestFunction = renderingAttr.getAlphaTestFunction();
    alphaTestValue = renderingAttr.getAlphaTestValue();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JCheckBox visibleCheckBox = new JCheckBox("Visible", visible);
    visibleCheckBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JCheckBox checkbox = (JCheckBox) e.getSource();
        visible = checkbox.isSelected();
        renderingAttr.setVisible(visible);
      }
    });
    // add the checkbox to the panel
    add(new LeftAlignComponent(visibleCheckBox));

    JCheckBox ignoreVertexColorsCheckBox = new JCheckBox(
        "Ignore Vertex Colors", ignoreVertexColors);
    ignoreVertexColorsCheckBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JCheckBox checkbox = (JCheckBox) e.getSource();
        ignoreVertexColors = checkbox.isSelected();
        renderingAttr.setIgnoreVertexColors(ignoreVertexColors);
      }
    });
    // add the checkbox to the panel
    add(new LeftAlignComponent(ignoreVertexColorsCheckBox));

    JCheckBox depthBufferEnableCheckBox = new JCheckBox(
        "Depth Buffer Enable", depthBufferEnable);
    depthBufferEnableCheckBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JCheckBox checkbox = (JCheckBox) e.getSource();
        depthBufferEnable = checkbox.isSelected();
        renderingAttr.setDepthBufferEnable(depthBufferEnable);
      }
    });
    // add the checkbox to the panel
    add(new LeftAlignComponent(depthBufferEnableCheckBox));
    // no cap bit for depth buffer enable
    depthBufferEnableCheckBox.setEnabled(false);

    JCheckBox depthBufferWriteEnableCheckBox = new JCheckBox(
        "Depth Buffer Write Enable", depthBufferWriteEnable);
    depthBufferWriteEnableCheckBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JCheckBox checkbox = (JCheckBox) e.getSource();
        depthBufferWriteEnable = checkbox.isSelected();
        renderingAttr.setDepthBufferWriteEnable(depthBufferWriteEnable);
      }
    });
    // add the checkbox to the panel
    add(new LeftAlignComponent(depthBufferWriteEnableCheckBox));
    // no cap bit for depth buffer enable
    depthBufferWriteEnableCheckBox.setEnabled(false);

    JCheckBox rasterOpEnableCheckBox = new JCheckBox(
        "Raster Operation Enable", rasterOpEnable);
    rasterOpEnableCheckBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JCheckBox checkbox = (JCheckBox) e.getSource();
        rasterOpEnable = checkbox.isSelected();
        renderingAttr.setRasterOpEnable(rasterOpEnable);
      }
    });
    // add the checkbox to the panel
    add(new LeftAlignComponent(rasterOpEnableCheckBox));

    String[] rasterOpNames = { "ROP_COPY", "ROP_XOR", };
    int[] rasterOpValues = { RenderingAttributes.ROP_COPY,
        RenderingAttributes.ROP_XOR, };
    IntChooser rasterOpChooser = new IntChooser("Raster Operation:",
        rasterOpNames, rasterOpValues, rasterOp);
    rasterOpChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        rasterOp = event.getValue();
        renderingAttr.setRasterOp(rasterOp);
      }
    });
    add(rasterOpChooser);

    String[] alphaTestFunctionNames = { "ALWAYS", "NEVER", "EQUAL",
        "NOT_EQUAL", "LESS", "LESS_OR_EQUAL", "GREATER",
        "GREATER_OR_EQUAL", };
    int[] alphaTestFunctionValues = { RenderingAttributes.ALWAYS,
        RenderingAttributes.NEVER, RenderingAttributes.EQUAL,
        RenderingAttributes.NOT_EQUAL, RenderingAttributes.LESS,
        RenderingAttributes.LESS_OR_EQUAL, RenderingAttributes.GREATER,
        RenderingAttributes.GREATER_OR_EQUAL, };
    IntChooser alphaTestFunctionChooser = new IntChooser(
        "Alpha Test Function:", alphaTestFunctionNames,
        alphaTestFunctionValues, alphaTestFunction);
    alphaTestFunctionChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        alphaTestFunction = event.getValue();
        renderingAttr.setAlphaTestFunction(alphaTestFunction);
      }
    });
    add(alphaTestFunctionChooser);

    FloatLabelJSlider alphaTestValueSlider = new FloatLabelJSlider(
        "Alpha Test Value: ", 0.1f, 0.0f, 1.0f, alphaTestValue);
    alphaTestValueSlider.setMajorTickSpacing(1.0f);
    alphaTestValueSlider.setPaintTicks(true);
    alphaTestValueSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        alphaTestValue = e.getValue();
        renderingAttr.setAlphaTestValue(alphaTestValue);
      }
    });
    add(alphaTestValueSlider);

  }
}

class FloatLabelJSlider extends JPanel implements ChangeListener,
    Java3DExplorerConstants {

  JSlider slider;

  JLabel valueLabel;

  Vector listeners = new Vector();

  float min, max, resolution, current, scale;

  int minInt, maxInt, curInt;;

  int intDigits, fractDigits;

  float minResolution = 0.001f;

  // default slider with name, resolution = 0.1, min = 0.0, max = 1.0 inital
  // 0.5
  FloatLabelJSlider(String name) {
    this(name, 0.1f, 0.0f, 1.0f, 0.5f);
  }

  FloatLabelJSlider(String name, float resolution, float min, float max,
      float current) {

    this.resolution = resolution;
    this.min = min;
    this.max = max;
    this.current = current;

    if (resolution < minResolution) {
      resolution = minResolution;
    }

    // round scale to nearest integer fraction. i.e. 0.3 => 1/3 = 0.33
    scale = (float) Math.round(1.0f / resolution);
    resolution = 1.0f / scale;

    // get the integer versions of max, min, current
    minInt = Math.round(min * scale);
    maxInt = Math.round(max * scale);
    curInt = Math.round(current * scale);

    // sliders use integers, so scale our floating point value by "scale"
    // to make each slider "notch" be "resolution". We will scale the
    // value down by "scale" when we get the event.
    slider = new JSlider(JSlider.HORIZONTAL, minInt, maxInt, curInt);
    slider.addChangeListener(this);

    valueLabel = new JLabel(" ");

    // set the initial value label
    setLabelString();

    // add min and max labels to the slider
    Hashtable labelTable = new Hashtable();
    labelTable.put(new Integer(minInt), new JLabel(nf.format(min)));
    labelTable.put(new Integer(maxInt), new JLabel(nf.format(max)));
    slider.setLabelTable(labelTable);
    slider.setPaintLabels(true);

    // layout to align left 
    setLayout(new BorderLayout());
    Box box = new Box(BoxLayout.X_AXIS);
    add(box, BorderLayout.WEST);

    box.add(new JLabel(name));
    box.add(slider);
    box.add(valueLabel);
  }

  public void setMinorTickSpacing(float spacing) {
    int intSpacing = Math.round(spacing * scale);
    slider.setMinorTickSpacing(intSpacing);
  }

  public void setMajorTickSpacing(float spacing) {
    int intSpacing = Math.round(spacing * scale);
    slider.setMajorTickSpacing(intSpacing);
  }

  public void setPaintTicks(boolean paint) {
    slider.setPaintTicks(paint);
  }

  public void addFloatListener(FloatListener listener) {
    listeners.add(listener);
  }

  public void removeFloatListener(FloatListener listener) {
    listeners.remove(listener);
  }

  public void stateChanged(ChangeEvent e) {
    JSlider source = (JSlider) e.getSource();
    // get the event type, set the corresponding value.
    // Sliders use integers, handle floating point values by scaling the
    // values by "scale" to allow settings at "resolution" intervals.
    // Divide by "scale" to get back to the real value.
    curInt = source.getValue();
    current = curInt / scale;

    valueChanged();
  }

  public void setValue(float newValue) {
    boolean changed = (newValue != current);
    current = newValue;
    if (changed) {
      valueChanged();
    }
  }

  private void valueChanged() {
    // update the label
    setLabelString();

    // notify the listeners
    FloatEvent event = new FloatEvent(this, current);
    for (Enumeration e = listeners.elements(); e.hasMoreElements();) {
      FloatListener listener = (FloatListener) e.nextElement();
      listener.floatChanged(event);
    }
  }

  void setLabelString() {
    // Need to muck around to try to make sure that the width of the label
    // is wide enough for the largest value. Pad the string
    // be large enough to hold the largest value.
    int pad = 5; // fudge to make up for variable width fonts
    float maxVal = Math.max(Math.abs(min), Math.abs(max));
    intDigits = Math.round((float) (Math.log(maxVal) / Math.log(10))) + pad;
    if (min < 0) {
      intDigits++; // add one for the '-'
    }
    // fractDigits is num digits of resolution for fraction. Use base 10 log
    // of scale, rounded up, + 2.
    fractDigits = (int) Math.ceil((Math.log(scale) / Math.log(10)));
    nf.setMinimumFractionDigits(fractDigits);
    nf.setMaximumFractionDigits(fractDigits);
    String value = nf.format(current);
    while (value.length() < (intDigits + fractDigits)) {
      value = value + "  ";
    }
    valueLabel.setText(value);
  }

}

class FloatEvent extends EventObject {

  float value;

  FloatEvent(Object source, float newValue) {
    super(source);
    value = newValue;
  }

  float getValue() {
    return value;
  }
}

interface FloatListener extends EventListener {
  void floatChanged(FloatEvent e);
}

class MaterialEditor extends Box implements Java3DExplorerConstants {

  Material material;

  boolean lightingEnable;

  Color3f ambientColor = new Color3f();

  Color3f diffuseColor = new Color3f();

  Color3f emissiveColor = new Color3f();

  Color3f specularColor = new Color3f();

  float shininess;

  JCheckBox lightingEnableCheckBox;

  Color3fEditor ambientEditor;

  Color3fEditor diffuseEditor;

  Color3fEditor emissiveEditor;

  Color3fEditor specularEditor;

  FloatLabelJSlider shininessSlider;

  public MaterialEditor(Material init) {
    super(BoxLayout.Y_AXIS);
    material = init;
    lightingEnable = material.getLightingEnable();
    material.getAmbientColor(ambientColor);
    material.getDiffuseColor(diffuseColor);
    material.getEmissiveColor(emissiveColor);
    material.getSpecularColor(specularColor);
    shininess = material.getShininess();

    lightingEnableCheckBox = new JCheckBox("Lighting Enable",
        lightingEnable);
    lightingEnableCheckBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JCheckBox checkbox = (JCheckBox) e.getSource();
        lightingEnable = checkbox.isSelected();
        material.setLightingEnable(lightingEnable);
      }
    });
    // add the checkbox to the panel
    add(new LeftAlignComponent(lightingEnableCheckBox));

    ambientEditor = new Color3fEditor("Ambient Color  ", ambientColor);
    ambientEditor.addColor3fListener(new Color3fListener() {
      public void colorChanged(Color3fEvent event) {
        event.getValue(ambientColor);
        material.setAmbientColor(ambientColor);
      }
    });
    add(ambientEditor);

    diffuseEditor = new Color3fEditor("Diffuse Color    ", diffuseColor);
    diffuseEditor.addColor3fListener(new Color3fListener() {
      public void colorChanged(Color3fEvent event) {
        event.getValue(diffuseColor);
        material.setDiffuseColor(diffuseColor);
      }
    });
    add(diffuseEditor);

    emissiveEditor = new Color3fEditor("Emissive Color", emissiveColor);
    emissiveEditor.addColor3fListener(new Color3fListener() {
      public void colorChanged(Color3fEvent event) {
        event.getValue(emissiveColor);
        material.setEmissiveColor(emissiveColor);
      }
    });
    add(emissiveEditor);

    specularEditor = new Color3fEditor("Specular Color ", specularColor);
    specularEditor.addColor3fListener(new Color3fListener() {
      public void colorChanged(Color3fEvent event) {
        event.getValue(specularColor);
        material.setSpecularColor(specularColor);
      }
    });
    add(specularEditor);

    shininessSlider = new FloatLabelJSlider("Shininess: ", 1.0f, 0.0f,
        128.0f, shininess);
    shininessSlider.setMajorTickSpacing(16.0f);
    shininessSlider.setPaintTicks(true);
    shininessSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        shininess = e.getValue();
        material.setShininess(shininess);
      }
    });
    add(shininessSlider);
  }
}

class TransparencyAttributesEditor extends JPanel implements
    Java3DExplorerConstants {

  // TransparencyAttributes
  TransparencyAttributes transpAttr;

  float transparency;

  int mode;

  int srcBlendFunction;

  int dstBlendFunction;

  TransparencyAttributesEditor(TransparencyAttributes init) {
    transpAttr = init;
    transparency = transpAttr.getTransparency();
    mode = transpAttr.getTransparencyMode();
    srcBlendFunction = transpAttr.getSrcBlendFunction();
    dstBlendFunction = transpAttr.getDstBlendFunction();

    setLayout(new GridLayout(4, 1));

    FloatLabelJSlider transparencySlider = new FloatLabelJSlider(
        "Transparency", 0.1f, 0.0f, 1.0f, transparency);
    transparencySlider.setMajorTickSpacing(0.1f);
    transparencySlider.setPaintTicks(true);
    transparencySlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        transparency = e.getValue();
        transpAttr.setTransparency(transparency);
      }
    });
    add(transparencySlider);

    String[] modeNames = { "NONE", "SCREEN_DOOR", "BLENDED", "NICEST",
        "FASTEST" };
    int[] modeValues = { TransparencyAttributes.NONE,
        TransparencyAttributes.SCREEN_DOOR,
        TransparencyAttributes.BLENDED, TransparencyAttributes.NICEST,
        TransparencyAttributes.FASTEST };

    IntChooser modeChooser = new IntChooser("Mode:", modeNames, modeValues,    mode);
    modeChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        mode = event.getValue();
        transpAttr.setTransparencyMode(mode);
      }
    });
    add(modeChooser);

    String[] blendNames = { "BLEND_ZERO", "BLEND_ONE", "BLEND_SRC_ALPHA",
        "BLEND_ONE_MINUS_SRC_ALPHA" };
    int[] blendValues = { TransparencyAttributes.BLEND_ZERO,
        TransparencyAttributes.BLEND_ONE,
        TransparencyAttributes.BLEND_SRC_ALPHA,
        TransparencyAttributes.BLEND_ONE_MINUS_SRC_ALPHA, };
    IntChooser srcBlendFunctionChooser = new IntChooser("Src Blend Func:",
        blendNames, blendValues, srcBlendFunction);
    srcBlendFunctionChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        srcBlendFunction = event.getValue();
        transpAttr.setSrcBlendFunction(srcBlendFunction);
      }
    });
    add(srcBlendFunctionChooser);
    IntChooser dstBlendFunctionChooser = new IntChooser("Dst Blend Func:",
        blendNames, blendValues, dstBlendFunction);
    dstBlendFunctionChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        dstBlendFunction = event.getValue();
        transpAttr.setDstBlendFunction(dstBlendFunction);
      }
    });
    add(dstBlendFunctionChooser);
  }
}

class Texture2DEditor extends Box implements Java3DExplorerConstants {

  Appearance appearance;

  Texture2D texture;

  String codeBaseString;

  String imageFile;

  String[] imageNames;

  String[] imageFileNames;

  int imageIndex;

  TextureLoader texLoader;

  int width;

  int height;

  int format;

  boolean enable;

  Color4f boundaryColor = new Color4f();

  int boundaryModeS;

  int boundaryModeT;

  int minFilter;

  int magFilter;

  int mipMapMode;

  public Texture2DEditor(Appearance app, String codeBaseString,
      String[] texImageNames, String[] texImageFileNames,
      int texImageIndex, boolean texEnable, int texBoundaryModeS,
      int texBoundaryModeT, int texMinFilter, int texMagFilter,
      int texMipMapMode, Color4f texBoundaryColor) {

    super(BoxLayout.Y_AXIS);

    this.appearance = app;
    // TODO: make deep copies?
    this.imageNames = texImageNames;
    this.imageFileNames = texImageFileNames;
    this.imageIndex = texImageIndex;
    this.imageFile = texImageFileNames[texImageIndex];
    this.codeBaseString = codeBaseString;
    this.enable = texEnable;
    this.mipMapMode = texMipMapMode;
    this.boundaryModeS = texBoundaryModeS;
    this.boundaryModeT = texBoundaryModeT;
    this.minFilter = texMinFilter;
    this.magFilter = texMagFilter;
    this.boundaryColor.set(texBoundaryColor);

    // set up the initial texture
    setTexture();

    JCheckBox texEnableCheckBox = new JCheckBox("Enable Texture");
    texEnableCheckBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        enable = ((JCheckBox) e.getSource()).isSelected();
        // workaround for bug
        // should just be able to
        // texture.setEnable(texEnable);
        // instead we have to:
        setTexture();
      }
    });

    // add the checkbox to the panel
    add(new LeftAlignComponent(texEnableCheckBox));

    IntChooser imgChooser = new IntChooser("Image:", imageNames);
    imgChooser.setValue(imageIndex);
    imgChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        imageIndex = event.getValue();
        imageFile = imageFileNames[imageIndex];
        setTexture();
      }
    });
    add(imgChooser);

    // texture boundaries
    String[] boundaryNames = { "WRAP", "CLAMP", };
    int[] boundaryValues = { Texture.WRAP, Texture.CLAMP, };

    // texture boundary S
    IntChooser bndSChooser = new IntChooser("Boundary S Mode:",
        boundaryNames, boundaryValues);
    bndSChooser.setValue(texBoundaryModeS);
    bndSChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        int value = event.getValue();
        boundaryModeS = value;
        setTexture();
      }
    });
    add(bndSChooser);

    // texture boundary T
    IntChooser bndTChooser = new IntChooser("Boundary T Mode:",
        boundaryNames, boundaryValues);
    bndTChooser.setValue(texBoundaryModeT);
    bndTChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        int value = event.getValue();
        boundaryModeT = value;
        setTexture();
      }
    });
    add(bndTChooser);

    // texture min filter
    String[] minFiltNames = { "FASTEST", "NICEST", "BASE_LEVEL_POINT",
        "BASE_LEVEL_LINEAR", "MULTI_LEVEL_POINT", "MULTI_LEVEL_LINEAR", };
    int[] minFiltValues = { Texture.FASTEST, Texture.NICEST,
        Texture.BASE_LEVEL_POINT, Texture.BASE_LEVEL_LINEAR,
        Texture.MULTI_LEVEL_POINT, Texture.MULTI_LEVEL_LINEAR, };

    // min filter
    IntChooser minFiltChooser = new IntChooser("Min Filter:", minFiltNames,
        minFiltValues);
    minFiltChooser.setValue(minFilter);
    minFiltChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        int value = event.getValue();
        minFilter = value;
        setTexture();
      }
    });
    add(minFiltChooser);

    // texture mag filter
    String[] magFiltNames = { "FASTEST", "NICEST", "BASE_LEVEL_POINT",
        "BASE_LEVEL_LINEAR", };
    int[] magFiltValues = { Texture.FASTEST, Texture.NICEST,
        Texture.BASE_LEVEL_POINT, Texture.BASE_LEVEL_LINEAR, };

    // mag filter
    IntChooser magFiltChooser = new IntChooser("Mag Filter:", magFiltNames,
        magFiltValues);
    magFiltChooser.setValue(magFilter);
    magFiltChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        int value = event.getValue();
        magFilter = value;
        setTexture();
      }
    });
    add(magFiltChooser);

    // texture mipmap mode
    String[] mipMapNames = { "BASE_LEVEL", "MULTI_LEVEL_MIPMAP", };
    int[] mipMapValues = { Texture.BASE_LEVEL, Texture.MULTI_LEVEL_MIPMAP, };

    // mipMap mode
    IntChooser mipMapChooser = new IntChooser("MipMap Mode:", mipMapNames,
        mipMapValues);
    mipMapChooser.setValue(mipMapMode);
    mipMapChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {    int value = event.getValue();
        mipMapMode = value;
        setTexture();
      }
    });
    add(mipMapChooser);

    Color4fEditor boundaryColorEditor = new Color4fEditor("Boundary Color",
        boundaryColor);
    boundaryColorEditor.addColor4fListener(new Color4fListener() {
      public void colorChanged(Color4fEvent event) {
        event.getValue(boundaryColor);
        setTexture();
      }
    });
    add(boundaryColorEditor);
  }

  // create a Texture2D using the current values from the GUI
  // and attach it to the appearance
  void setTexture() {
    // set up the image using the TextureLoader
    java.net.URL imageURL = null;
    try {
      imageURL = new java.net.URL(codeBaseString + imageFile);
    } catch (Exception e) {
      System.err.println("Exception: " + e);
      System.exit(1);
    }
    int flags;
    if (mipMapMode == Texture.BASE_LEVEL) {
      flags = 0;
    } else {
      flags = TextureLoader.GENERATE_MIPMAP;
    }
    texLoader = new TextureLoader(imageURL, new String("RGBA"), flags, this);

    // We could create texture from image
    //
    // Get the image from the loader. We need an image which
    // has power of two dimensions, so we'll get the unscaled image,
    // figure out what the scaled size should be and then get a scale
    // image
    //ImageComponent2D unscaledImage = texLoader.getImage();
    //int width = unscaledImage.getWidth();
    //int height = unscaledImage.getWidth();
    //
    // scaled values are next power of two greater than or equal to
    // value
    //texWidth = powerOfTwo(width);
    //texHeight = powerOfTwo(height);
    //
    // rescale the image if necessary
    //ImageComponent2D texImage;
    //if ((texWidth == width) && (texHeight == height)) {
    //    texImage = unscaledImage;
    //} else {
    //    texImage = texLoader.getScaledImage(texWidth, texHeight);
    //}
    //texFormat = Texture.RGB;
    //texture = new Texture2D(texMipMapMode, texFormat, texWidth,
    //  texHeight);
    //texture.setImage(0, texImage);

    // instead we'll just get get the texture from loader
    texture = (Texture2D) texLoader.getTexture();

    texture.setBoundaryColor(boundaryColor);
    texture.setBoundaryModeS(boundaryModeS);
    texture.setBoundaryModeT(boundaryModeT);
    texture.setEnable(enable);
    texture.setMinFilter(minFilter);
    texture.setMagFilter(magFilter);

    // Set the capabilities to enable the changable attrs
    texture.setCapability(Texture.ALLOW_ENABLE_WRITE);
    texture.setCapability(Texture.ALLOW_IMAGE_WRITE);

    // connect the new texture to the appearance
    appearance.setTexture(texture);
  }

}

class TextureAttributesEditor extends Box implements Java3DExplorerConstants {

  // TextureAttributes
  TextureAttributes textureAttr;

  float transparency;

  int mode;

  int pcMode;

  Color4f blendColor = new Color4f();

  TextureAttributesEditor(TextureAttributes init) {
    super(BoxLayout.Y_AXIS);
    textureAttr = init;
    mode = textureAttr.getTextureMode();
    pcMode = textureAttr.getPerspectiveCorrectionMode();
    textureAttr.getTextureBlendColor(blendColor);

    String[] modeNames = { "REPLACE", "MODULATE", "DECAL", "BLEND", };
    int[] modeValues = { TextureAttributes.REPLACE,
        TextureAttributes.MODULATE, TextureAttributes.DECAL,
        TextureAttributes.BLEND, };

    IntChooser modeChooser = new IntChooser("Mode:", modeNames, modeValues,
        mode);
    modeChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        mode = event.getValue();
        textureAttr.setTextureMode(mode);
      }
    });
    add(modeChooser);

    Color4fEditor blendColorEditor = new Color4fEditor("Blend Color",
        blendColor);
    blendColorEditor.addColor4fListener(new Color4fListener() {
      public void colorChanged(Color4fEvent event) {
        event.getValue(blendColor);
        textureAttr.setTextureBlendColor(blendColor);
      }
    });
    add(blendColorEditor);

    String[] pcModeNames = { "NICEST", "FASTEST", };
    int[] pcModeValues = { TextureAttributes.NICEST,
        TextureAttributes.FASTEST, };

    IntChooser pcModeChooser = new IntChooser("Perspective Correction:",
        pcModeNames, pcModeValues, pcMode);
    pcModeChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        pcMode = event.getValue();
        textureAttr.setPerspectiveCorrectionMode(pcMode);
      }
    });
    add(pcModeChooser);

  }
}

//
//
//Note: this editor only handles 2D tex gen
//

class TexCoordGenerationEditor extends Box implements Java3DExplorerConstants {

  // TexCoordGeneration
  Appearance app;

  TexCoordGeneration texGen;

  boolean enable;

  int mode;

  Vector4f planeS = new Vector4f();

  Vector4f planeT = new Vector4f();

  TexCoordGenerationEditor(Appearance initApp, boolean initEnable,
      int initMode, Vector4f initPlaneS, Vector4f initPlaneT) {

    super(BoxLayout.Y_AXIS);
    app = initApp;
    enable = initEnable;
    mode = initMode;
    planeS.set(initPlaneS);
    planeT.set(initPlaneT);
    setTexGen(); // set up the initial texGen

    JCheckBox enableCheckBox = new JCheckBox("Enable Tex Coord Gen");
    enableCheckBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        enable = ((JCheckBox) e.getSource()).isSelected();
        texGen.setEnable(enable);
      }
    });
    add(new LeftAlignComponent(enableCheckBox));

    // texture boundaries
    String[] modeNames = { "OBJECT_LINEAR", "EYE_LINEAR", "SPHERE_MAP", };
    int[] modeValues = { TexCoordGeneration.OBJECT_LINEAR,
        TexCoordGeneration.EYE_LINEAR, TexCoordGeneration.SPHERE_MAP, };

    // tex gen modes
    IntChooser modeChooser = new IntChooser("Generation Mode:", modeNames,
        modeValues);
    modeChooser.setValue(mode);
    modeChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        int value = event.getValue();
        mode = value;
        setTexGen();
      }
    });
    add(modeChooser);

    // make a panel for both sets of sliders and then two sub-panels,
    // one for each group of sliders
    Box sliderPanel = new Box(BoxLayout.Y_AXIS);
    add(sliderPanel);

    Box planeSPanel = new Box(BoxLayout.Y_AXIS);
    Box planeTPanel = new Box(BoxLayout.Y_AXIS);
    sliderPanel.add(planeSPanel);
    sliderPanel.add(planeTPanel);

    planeSPanel.add(new LeftAlignComponent(new JLabel("Plane S:")));
    FloatLabelJSlider planeSxSlider = new FloatLabelJSlider("X:", 0.1f,
        -10.0f, 10.0f, planeS.x);
    planeSxSlider.setMajorTickSpacing(0.1f);
    planeSxSlider.setPaintTicks(true);
    planeSxSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        planeS.x = e.getValue();
        setTexGen();
      }
    });
    planeSPanel.add(planeSxSlider);

    FloatLabelJSlider planeSySlider = new FloatLabelJSlider("Y:", 0.1f,
        -10.0f, 10.0f, planeS.y);
    planeSySlider.setMajorTickSpacing(0.1f);
    planeSySlider.setPaintTicks(true);
    planeSySlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        planeS.y = e.getValue();
        setTexGen();
      }
    });
    planeSPanel.add(planeSySlider);

    FloatLabelJSlider planeSzSlider = new FloatLabelJSlider("Z:", 0.1f,
        -10.0f, 10.0f, planeS.z);
    planeSzSlider.setMajorTickSpacing(0.1f);
    planeSzSlider.setPaintTicks(true);
    planeSzSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        planeS.z = e.getValue();
        setTexGen();
      }
    });
    planeSPanel.add(planeSzSlider);

    FloatLabelJSlider planeSwSlider = new FloatLabelJSlider("W:", 0.1f,
        -10.0f, 10.0f, planeS.w);
    planeSwSlider.setMajorTickSpacing(0.1f);
    planeSwSlider.setPaintTicks(true);
    planeSwSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        planeS.w = e.getValue();
        setTexGen();
      }
    });
    planeSPanel.add(planeSwSlider);

    planeSPanel.add(new LeftAlignComponent(new JLabel("Plane T:")));
    FloatLabelJSlider planeTxSlider = new FloatLabelJSlider("X:", 0.1f,
        -10.0f, 10.0f, planeT.x);
    planeTxSlider.setMajorTickSpacing(0.1f);
    planeTxSlider.setPaintTicks(true);
    planeTxSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        planeT.x = e.getValue();
        setTexGen();
      }
    });
    planeTPanel.add(planeTxSlider);

    FloatLabelJSlider planeTySlider = new FloatLabelJSlider("Y:", 0.1f,
        -10.0f, 10.0f, planeT.y);
    planeTySlider.setMajorTickSpacing(0.1f);
    planeTySlider.setPaintTicks(true);
    planeTySlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        planeT.y = e.getValue();
        setTexGen();
      }
    });
    planeTPanel.add(planeTySlider);

    FloatLabelJSlider planeTzSlider = new FloatLabelJSlider("Z:", 0.1f,
        -10.0f, 10.0f, planeT.z);
    planeTzSlider.setMajorTickSpacing(0.1f);
    planeTzSlider.setPaintTicks(true);
    planeTzSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        planeT.z = e.getValue();
        setTexGen();
      }
    });
    planeTPanel.add(planeTzSlider);

    FloatLabelJSlider planeTwSlider = new FloatLabelJSlider("W:", 0.1f,
        -10.0f, 10.0f, planeT.w);
    planeTwSlider.setMajorTickSpacing(0.1f);
    planeTwSlider.setPaintTicks(true);
    planeTwSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        planeT.w = e.getValue();
        setTexGen();
      }
    });
    planeTPanel.add(planeTwSlider);
  }

  void setTexGen() {
    texGen = new TexCoordGeneration(mode,
        TexCoordGeneration.TEXTURE_COORDINATE_2, planeS, planeT);
    texGen.setCapability(TexCoordGeneration.ALLOW_ENABLE_WRITE);
    texGen.setEnable(enable);
    app.setTexCoordGeneration(texGen);
  }
}

class MaterialPresetEditor extends MaterialEditor implements ActionListener {

  String[] materialNames;

  Material[] materialPresets;

  IntChooser presetChooser;

  public MaterialPresetEditor(Material init, String[] presetNames,
      Material[] presets) {

    super(init);
    if ((presetNames.length != presets.length)) {
      throw new IllegalArgumentException(
          "Preset name and value arrays must have the same length");
    }
    materialNames = presetNames;
    materialPresets = presets;

    JPanel presetPanel = new JPanel();

    presetChooser = new IntChooser("Preset:", materialNames);
    presetPanel.add(presetChooser);

    JButton presetCopyButton = new JButton("Copy preset");
    presetCopyButton.addActionListener(this);
    presetPanel.add(presetCopyButton);

    add(new LeftAlignComponent(presetPanel));
  }

  // copy when button is pressed
  public void actionPerformed(ActionEvent e) {
    Material copyMaterial = materialPresets[presetChooser.getValue()];

    lightingEnable = copyMaterial.getLightingEnable();
    copyMaterial.getAmbientColor(ambientColor);
    copyMaterial.getDiffuseColor(diffuseColor);
    copyMaterial.getEmissiveColor(emissiveColor);
    copyMaterial.getSpecularColor(specularColor);
    shininess = copyMaterial.getShininess();

    // update the GUI
    lightingEnableCheckBox.setSelected(lightingEnable);
    material.setLightingEnable(lightingEnable);
    ambientEditor.setValue(ambientColor);
    diffuseEditor.setValue(diffuseColor);
    emissiveEditor.setValue(emissiveColor);
    specularEditor.setValue(specularColor);
    shininessSlider.setValue(shininess);
  }
}

class LeftAlignComponent extends JPanel {
  LeftAlignComponent(Component c) {
    setLayout(new BorderLayout());
    add(c, BorderLayout.WEST);
  }
}

class BackgroundTool implements Java3DExplorerConstants {

  Switch bgSwitch;

  IntChooser bgChooser;

  BackgroundTool(String codeBaseString) {

    bgSwitch = new Switch(Switch.CHILD_NONE);
    bgSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);

    // set up the dark grey BG color node
    Background bgDarkGrey = new Background(darkGrey);
    bgDarkGrey.setApplicationBounds(infiniteBounds);
    bgSwitch.addChild(bgDarkGrey);

    // set up the grey BG color node
    Background bgGrey = new Background(grey);
    bgGrey.setApplicationBounds(infiniteBounds);
    bgSwitch.addChild(bgGrey);

    // set up the light grey BG color node
    Background bgLightGrey = new Background(lightGrey);
    bgLightGrey.setApplicationBounds(infiniteBounds);
    bgSwitch.addChild(bgLightGrey);

    // set up the white BG color node
    Background bgWhite = new Background(white);
    bgWhite.setApplicationBounds(infiniteBounds);
    bgSwitch.addChild(bgWhite);

    // set up the blue BG color node
    Background bgBlue = new Background(skyBlue);
    bgBlue.setApplicationBounds(infiniteBounds);
    bgSwitch.addChild(bgBlue);

    // set up the image
    java.net.URL bgImageURL = null;
    try {
      bgImageURL = new java.net.URL(codeBaseString + "bg.jpg");
    } catch (java.net.MalformedURLException ex) {
      System.out.println(ex.getMessage());
      System.exit(1);
    }
    if (bgImageURL == null) { // application, try file URL
      try {
        bgImageURL = new java.net.URL("file:./bg.jpg");
      } catch (java.net.MalformedURLException ex) {
        System.out.println(ex.getMessage());
        System.exit(1);
      }
    }
    TextureLoader bgTexture = new TextureLoader(bgImageURL, null);

    // Create a background with the static image
    Background bgImage = new Background(bgTexture.getImage());
    bgImage.setApplicationBounds(infiniteBounds);
    bgSwitch.addChild(bgImage);

    // create a background with the image mapped onto a sphere which
    // will enclose the world
    Background bgGeo = new Background();
    bgGeo.setApplicationBounds(infiniteBounds);
    BranchGroup bgGeoBG = new BranchGroup();
    Appearance bgGeoApp = new Appearance();
    bgGeoApp.setTexture(bgTexture.getTexture());
    Sphere sphereObj = new Sphere(1.0f, Sphere.GENERATE_NORMALS
        | Sphere.GENERATE_NORMALS_INWARD
        | Sphere.GENERATE_TEXTURE_COORDS, 45, bgGeoApp);
    bgGeoBG.addChild(sphereObj);
    bgGeo.setGeometry(bgGeoBG);
    bgSwitch.addChild(bgGeo);

    // Create the chooser GUI
    String[] bgNames = { "No Background (Black)", "Dark Grey", "Grey",
        "Light Grey", "White", "Blue", "Sky Image", "Sky Geometry", };
    int[] bgValues = { Switch.CHILD_NONE, 0, 1, 2, 3, 4, 5, 6 };

    bgChooser = new IntChooser("Background:", bgNames, bgValues, 0);
    bgChooser.addIntListener(new IntListener() {
      public void intChanged(IntEvent event) {
        int value = event.getValue();
        bgSwitch.setWhichChild(value);
      }
    });
    bgChooser.setValue(Switch.CHILD_NONE);
  }

  Switch getSwitch() {
    return bgSwitch;
  }

  IntChooser getChooser() {
    return bgChooser;
  }

}

class LogFloatLabelJSlider extends JPanel implements ChangeListener,
    Java3DExplorerConstants {

  JSlider slider;

  JLabel valueLabel;

  Vector listeners = new Vector();

  float min, max, resolution, current, scale;

  double minLog, maxLog, curLog;

  int minInt, maxInt, curInt;;

  int intDigits, fractDigits;

  NumberFormat nf = NumberFormat.getInstance();

  float minResolution = 0.001f;

  double logBase = Math.log(10);

  // default slider with name, resolution = 0.1, min = 0.0, max = 1.0 inital
  // 0.5
  LogFloatLabelJSlider(String name) {
    this(name, 0.1f, 100.0f, 10.0f);
  }

  LogFloatLabelJSlider(String name, float min, float max, float current) {

    this.resolution = resolution;
    this.min = min;
    this.max = max;
    this.current = current;

    if (resolution < minResolution) {
      resolution = minResolution;
    }

    minLog = log10(min);
    maxLog = log10(max);
    curLog = log10(current);

    // resolution is 100 steps from min to max
    scale = 100.0f;
    resolution = 1.0f / scale;

    // get the integer versions of max, min, current
    minInt = (int) Math.round(minLog * scale);
    maxInt = (int) Math.round(maxLog * scale);
    curInt = (int) Math.round(curLog * scale);

    slider = new JSlider(JSlider.HORIZONTAL, minInt, maxInt, curInt);
    slider.addChangeListener(this);

    valueLabel = new JLabel(" ");

    // Need to muck around to make sure that the width of the label
    // is wide enough for the largest value. Pad the initial string
    // be large enough to hold the largest value.
    int pad = 5; // fudge to make up for variable width fonts
    intDigits = (int) Math.ceil(maxLog) + pad;
    if (min < 0) {
      intDigits++; // add one for the '-'
    }
    if (minLog < 0) {
      fractDigits = (int) Math.ceil(-minLog);
    } else {
      fractDigits = 0;
    }
    nf.setMinimumFractionDigits(fractDigits);
    nf.setMaximumFractionDigits(fractDigits);
    String value = nf.format(current);
    while (value.length() < (intDigits + fractDigits)) {
      value = value + " ";
    }
    valueLabel.setText(value);

    // add min and max labels to the slider
    Hashtable labelTable = new Hashtable();
    labelTable.put(new Integer(minInt), new JLabel(nf.format(min)));
    labelTable.put(new Integer(maxInt), new JLabel(nf.format(max)));
    slider.setLabelTable(labelTable);
    slider.setPaintLabels(true);

    // layout to align left
    setLayout(new BorderLayout());
    Box box = new Box(BoxLayout.X_AXIS);
    add(box, BorderLayout.WEST);

    box.add(new JLabel(name));
    box.add(slider);
    box.add(valueLabel);
  }

  public void setMinorTickSpacing(float spacing) {
    int intSpacing = Math.round(spacing * scale);
    slider.setMinorTickSpacing(intSpacing);
  }

  public void setMajorTickSpacing(float spacing) {
    int intSpacing = Math.round(spacing * scale);
    slider.setMajorTickSpacing(intSpacing);
  }

  public void setPaintTicks(boolean paint) {
    slider.setPaintTicks(paint);
  }

  public void addFloatListener(FloatListener listener) {
    listeners.add(listener);
  }

  public void removeFloatListener(FloatListener listener) {
    listeners.remove(listener);
  }

  public void stateChanged(ChangeEvent e) {
    JSlider source = (JSlider) e.getSource();
    curInt = source.getValue();
    curLog = curInt / scale;
    current = (float) exp10(curLog);

    valueChanged();
  }

  public void setValue(float newValue) {
    boolean changed = (newValue != current);
    current = newValue;
    if (changed) {
      valueChanged();
    }
  }

  private void valueChanged() {
    String value = nf.format(current);
    valueLabel.setText(value);

    // notify the listeners
    FloatEvent event = new FloatEvent(this, current);
    for (Enumeration e = listeners.elements(); e.hasMoreElements();) {
      FloatListener listener = (FloatListener) e.nextElement();
      listener.floatChanged(event);
    }
  }

  double log10(double value) {
    return Math.log(value) / logBase;
  }

  double exp10(double value) {
    return Math.exp(value * logBase);
  }

}

interface Color4fListener extends EventListener {
  void colorChanged(Color4fEvent e);
}

class Color4fEvent extends EventObject {

  Color4f value = new Color4f();

  Color4fEvent(Object source, Color4f newValue) {
    super(source);
    value.set(newValue);
  }

  void getValue(Color4f getValue) {
    getValue.set(value);
  }
}

class Color4fEditor extends Box implements ActionListener,
    Java3DExplorerConstants {

  String name;

  Color4f color = new Color4f();

  Color3f color3f = new Color3f(); // just RGB of Color4f

  JButton button;

  JPanel preview;

  Vector listeners = new Vector();

  public Color4fEditor(String initName, Color4f initColor) {
    super(BoxLayout.Y_AXIS);
    name = initName;
    color.set(initColor);
    color3f.x = color.x;
    color3f.y = color.y;
    color3f.z = color.z;

    JPanel colorPanel = new JPanel();
    colorPanel.setLayout(new BorderLayout());
    add(colorPanel);

    JLabel label = new JLabel(name);

    preview = new JPanel();
    preview.setPreferredSize(new Dimension(40, 40));
    preview.setBackground(color3f.get());
    preview.setBorder(BorderFactory.createRaisedBevelBorder());

    button = new JButton("Set");
    button.addActionListener(this);

    JPanel filler = new JPanel();
    filler.setPreferredSize(new Dimension(100, 20));

    Box box = new Box(BoxLayout.X_AXIS);
    colorPanel.add(box, BorderLayout.WEST);

    box.add(label);
    box.add(preview);
    box.add(button);
    box.add(filler);

    FloatLabelJSlider alphaSlider = new FloatLabelJSlider("    Alpha");
    alphaSlider.setValue(color.w);
    alphaSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent event) {
        color.w = event.getValue();
        valueChanged();
      }
    });
    add(alphaSlider);
  }

  public void actionPerformed(ActionEvent e) {
    Color currentColor = color3f.get();
    Color newColor = JColorChooser.showDialog(this, name, currentColor);
    if (newColor != null) {
      color3f.set(newColor);
      color.x = color3f.x;
      color.y = color3f.y;
      color.z = color3f.z;
      valueChanged();
    }
  }

  public void setValue(Color4f newValue) {
    boolean changed = !color.equals(newValue);
    if (changed) {
      color.set(newValue);
      color3f.x = color.x;
      color3f.y = color.y;
      color3f.z = color.z;
      valueChanged();
    }
  }

  public void addColor4fListener(Color4fListener listener) {
    listeners.add(listener);
  }

  public void removeColor4fListener(Color4fListener listener) {
    listeners.remove(listener);
  }

  private void valueChanged() {
    // update the preview
    preview.setBackground(color3f.get());

    // notify the listeners
    Color4fEvent event = new Color4fEvent(this, color);
    for (Enumeration e = listeners.elements(); e.hasMoreElements();) {
      Color4fListener listener = (Color4fListener) e.nextElement();
      listener.colorChanged(event);
    }
  }
}
*/