package software.bernie.geckolib3.geo.render.built;

import java.util.ArrayList;
import java.util.List;

import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector4f;

import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.core.snapshot.BoneSnapshot;

public class GeoBone implements IBone {
	public GeoBone parent;

	public List<GeoBone> childBones = new ArrayList<>();
	public List<GeoCube> childCubes = new ArrayList<>();

	public String name;
	private BoneSnapshot initialSnapshot;

	public Boolean mirror;
	public Double inflate;
	public Boolean dontRender;
	public boolean isHidden;
	public boolean areCubesHidden = false;
	public boolean hideChildBonesToo;
	// I still have no idea what this field does, but its in the json file so
	// ¯\_(ツ)_/¯
	public Boolean reset;

	private float scaleX = 1;
	private float scaleY = 1;
	private float scaleZ = 1;

	private float positionX;
	private float positionY;
	private float positionZ;

	public float rotationPointX;
	public float rotationPointY;
	public float rotationPointZ;

	private float rotateX;
	private float rotateY;
	private float rotateZ;

	public Object extraData;

	private Matrix4f modelSpaceXform;
	private boolean trackXform;
	public Matrix4f rotMat;

	private Matrix4f worldSpaceXform;
	private Matrix3f worldSpaceNormal;

	public GeoBone() {
		modelSpaceXform = new Matrix4f();
		modelSpaceXform.setIdentity();
		trackXform = false;
		rotMat = null;

		worldSpaceXform = new Matrix4f();
		worldSpaceXform.setIdentity();
		worldSpaceNormal = new Matrix3f();
		worldSpaceNormal.setIdentity();
	}

	@Override
	public void setModelRendererName(String modelRendererName) {
		this.name = modelRendererName;
	}

	@Override
	public void saveInitialSnapshot() {
		if (this.initialSnapshot == null) {
			this.initialSnapshot = new BoneSnapshot(this, true);
		}
	}

	@Override
	public BoneSnapshot getInitialSnapshot() {
		return this.initialSnapshot;
	}

	@Override
	public String getName() {
		return this.name;
	}

	// Boilerplate code incoming

	@Override
	public float getRotationX() {
		return rotateX;
	}

	@Override
	public float getRotationY() {
		return rotateY;
	}

	@Override
	public float getRotationZ() {
		return rotateZ;
	}

	@Override
	public float getPositionX() {
		return positionX;
	}

	@Override
	public float getPositionY() {
		return positionY;
	}

	@Override
	public float getPositionZ() {
		return positionZ;
	}

	@Override
	public float getScaleX() {
		return scaleX;
	}

	@Override
	public float getScaleY() {
		return scaleY;
	}

	@Override
	public float getScaleZ() {
		return scaleZ;
	}

	@Override
	public void setRotationX(float value) {
		this.rotateX = value;
	}

	@Override
	public void setRotationY(float value) {
		this.rotateY = value;
	}

	@Override
	public void setRotationZ(float value) {
		this.rotateZ = value;
	}

	@Override
	public void setPositionX(float value) {
		this.positionX = value;
	}

	@Override
	public void setPositionY(float value) {
		this.positionY = value;
	}

	@Override
	public void setPositionZ(float value) {
		this.positionZ = value;
	}

	@Override
	public void setScaleX(float value) {
		this.scaleX = value;
	}

	@Override
	public void setScaleY(float value) {
		this.scaleY = value;
	}

	@Override
	public void setScaleZ(float value) {
		this.scaleZ = value;
	}

	@Override
	public boolean isHidden() {
		return this.isHidden;
	}

	@Override
	public void setHidden(boolean hidden) {
		this.setHidden(hidden, hidden);
	}

	@Override
	public void setPivotX(float value) {
		this.rotationPointX = value;
	}

	@Override
	public void setPivotY(float value) {
		this.rotationPointY = value;
	}

	@Override
	public void setPivotZ(float value) {
		this.rotationPointZ = value;
	}

	@Override
	public float getPivotX() {
		return this.rotationPointX;
	}

	@Override
	public float getPivotY() {
		return this.rotationPointY;
	}

	@Override
	public float getPivotZ() {
		return this.rotationPointZ;
	}

	@Override
	public boolean cubesAreHidden() {
		return areCubesHidden;
	}

	@Override
	public boolean childBonesAreHiddenToo() {
		return hideChildBonesToo;
	}

	@Override
	public void setCubesHidden(boolean hidden) {
		this.areCubesHidden = hidden;
	}

	@Override
	public void setHidden(boolean selfHidden, boolean skipChildRendering) {
		this.isHidden = selfHidden;
		this.hideChildBonesToo = skipChildRendering;
	}

	public GeoBone getParent() {
		return (GeoBone) parent;
	}

	public boolean isTrackingXform() {
		return trackXform;
	}

	public void setTrackXform(boolean trackXform) {
		this.trackXform = trackXform;
	}

	public Matrix4f getModelSpaceXform() {
		setTrackXform(true);
		return modelSpaceXform;
	}

	public void setModelSpaceXform(Matrix4f modelSpaceXform) {
		this.modelSpaceXform.load(modelSpaceXform);
	}

	public Vector3d getModelPosition() {
		Matrix4f matrix = getModelSpaceXform();
		Vector4f vec = new Vector4f(0, 0, 0, 1);
		vec.transform(matrix);
		return new Vector3d(-vec.x() * 16f, vec.y() * 16f, vec.z() * 16f);
	}

	public Matrix4f getWorldSpaceXform() {
		setTrackXform(true);
		return worldSpaceXform;
	}

	public void setWorldSpaceXform(Matrix4f worldSpaceXform) {
		this.worldSpaceXform.load(worldSpaceXform);
	}

	public Vector3d getWorldPosition() {
		Matrix4f matrix = getWorldSpaceXform();
		Vector4f vec = new Vector4f(0, 0, 0, 1);
		vec.transform(matrix);
		return new Vector3d(vec.x(), vec.y(), vec.z());
	}

	public void setModelPosition(Vector3d pos) {
		// TODO: Doesn't work on bones with parent transforms
		GeoBone parent = getParent();
		Matrix4f identity = new Matrix4f();
		identity.setIdentity();
		Matrix4f matrix = parent == null ? identity : parent.getModelSpaceXform().copy();
		matrix.invert();
		Vector4f vec = new Vector4f(-(float) pos.x / 16f, (float) pos.y / 16f, (float) pos.z / 16f, 1);
		vec.transform(matrix);
		setPosition(-vec.x() * 16f, vec.y() * 16f, vec.z() * 16f);
	}

	public Matrix4f getModelRotationMat() {
		Matrix4f matrix = getModelSpaceXform().copy();
		return matrix;
	}

	public static void removeMatrixTranslation(Matrix4f matrix) {
		matrix.m03 = 0;
		matrix.m13 = 0;
		matrix.m23 = 0;
	}

	public void setModelRotationMat(Matrix4f mat) {
		rotMat = mat;
	}

	public void setWorldSpaceNormal(Matrix3f worldSpaceNormal) {
		this.worldSpaceNormal = worldSpaceNormal;
	}

	public Matrix3f getWorldSpaceNormal() {
		return worldSpaceNormal;
	}

	// Position utils
	public void addPosition(Vector3d vec) {
		addPosition((float) vec.x, (float) vec.y, (float) vec.z);
	}

	public void addPosition(float x, float y, float z) {
		addPositionX(x);
		addPositionY(y);
		addPositionZ(z);
	}

	public void addPositionX(float x) {
		setPositionX(getPositionX() + x);
	}

	public void addPositionY(float y) {
		setPositionY(getPositionY() + y);
	}

	public void addPositionZ(float z) {
		setPositionZ(getPositionZ() + z);
	}

	public void setPosition(Vector3d vec) {
		setPosition((float) vec.x, (float) vec.y, (float) vec.z);
	}

	public void setPosition(float x, float y, float z) {
		setPositionX(x);
		setPositionY(y);
		setPositionZ(z);
	}

	public Vector3d getPosition() {
		return new Vector3d(getPositionX(), getPositionY(), getPositionZ());
	}

	// Rotation utils
	public void addRotation(Vector3d vec) {
		addRotation((float) vec.x, (float) vec.y, (float) vec.z);
	}

	public void addRotation(float x, float y, float z) {
		addRotationX(x);
		addRotationY(y);
		addRotationZ(z);
	}

	public void addRotationX(float x) {
		setRotationX(getRotationX() + x);
	}

	public void addRotationY(float y) {
		setRotationY(getRotationY() + y);
	}

	public void addRotationZ(float z) {
		setRotationZ(getRotationZ() + z);
	}

	public void setRotation(Vector3d vec) {
		setRotation((float) vec.x, (float) vec.y, (float) vec.z);
	}

	public void setRotation(float x, float y, float z) {
		setRotationX(x);
		setRotationY(y);
		setRotationZ(z);
	}

	public Vector3d getRotation() {
		return new Vector3d(getRotationX(), getRotationY(), getRotationZ());
	}

	// Scale utils
	public void multiplyScale(Vector3d vec) {
		multiplyScale((float) vec.x, (float) vec.y, (float) vec.z);
	}

	public void multiplyScale(float x, float y, float z) {
		setScaleX(getScaleX() * x);
		setScaleY(getScaleY() * y);
		setScaleZ(getScaleZ() * z);
	}

	public void setScale(Vector3d vec) {
		setScale((float) vec.x, (float) vec.y, (float) vec.z);
	}

	public void setScale(float x, float y, float z) {
		setScaleX(x);
		setScaleY(y);
		setScaleZ(z);
	}

	public Vector3d getScale() {
		return new Vector3d(getScaleX(), getScaleY(), getScaleZ());
	}

	public void addRotationOffsetFromBone(GeoBone source) {
		setRotationX(getRotationX() + source.getRotationX() - source.getInitialSnapshot().rotationValueX);
		setRotationY(getRotationY() + source.getRotationY() - source.getInitialSnapshot().rotationValueY);
		setRotationZ(getRotationZ() + source.getRotationZ() - source.getInitialSnapshot().rotationValueZ);
	}
}
