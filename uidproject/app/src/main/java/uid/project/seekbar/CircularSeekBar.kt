package uid.project.seekbar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import uid.project.R


class CircularSeekBar : View {

    protected val DPTOPX_SCALE = resources.displayMetrics.density
    protected val MIN_TOUCH_TARGET_DP = 48f
    protected var mCirclePaint: Paint? = null
    protected var mCircleFillPaint: Paint? = null
    protected var mCircleProgressPaint: Paint? = null
    protected var mCircleProgressGlowPaint: Paint? = null
    protected var mPointerPaint: Paint? = null
    protected var mPointerHaloPaint: Paint? = null
    protected var mPointerHaloBorderPaint: Paint? = null
    protected var mCircleStrokeWidth = 0f
    protected var mCircleXRadius = 0f
    protected var mCircleYRadius = 0f
    protected var mPointerRadius = 0f
    protected var mPointerHaloWidth = 0f
    protected var mPointerHaloBorderWidth = 0f
    protected var mStartAngle = 0f
    protected var mEndAngle = 0f
    protected var mCircleRectF = RectF()
    protected var mPointerColor = DEFAULT_POINTER_COLOR
    protected var mPointerHaloColor = DEFAULT_POINTER_HALO_COLOR
    protected var mPointerHaloColorOnTouch = DEFAULT_POINTER_HALO_COLOR_ONTOUCH
    protected var mCircleColor = DEFAULT_CIRCLE_COLOR
    protected var mCircleFillColor = DEFAULT_CIRCLE_FILL_COLOR
    protected var mCircleProgressColor = DEFAULT_CIRCLE_PROGRESS_COLOR
    protected var mPointerAlpha = DEFAULT_POINTER_ALPHA
    protected var mPointerAlphaOnTouch = DEFAULT_POINTER_ALPHA_ONTOUCH
    protected var mTotalCircleDegrees = 0f
    protected var mProgressDegrees = 0f
    protected var mCirclePath: Path? = null
    protected var mCircleProgressPath: Path? = null
    protected var mMax = 0
    protected var mProgress = 0
    protected var mCustomRadii = false
    protected var mMaintainEqualCircle = false
    protected var mMoveOutsideCircle = false
    var isLockEnabled = true
    protected var lockAtStart = true
    protected var lockAtEnd = false
    protected var mUserIsMovingPointer = false
    protected var cwDistanceFromStart = 0f
    protected var ccwDistanceFromStart = 0f
    protected var cwDistanceFromEnd = 0f

    @Suppress("unused")
    protected var ccwDistanceFromEnd = 0f

    protected var lastCWDistanceFromStart = 0f
    protected var cwDistanceFromPointer = 0f
    protected var ccwDistanceFromPointer = 0f
    protected var mIsMovingCW = false
    protected var mCircleWidth = 0f
    protected var mCircleHeight = 0f
    protected var mPointerPosition = 0f
    protected var mPointerPositionXY = FloatArray(2)

    protected var mOnCircularSeekBarChangeListener: OnCircularSeekBarChangeListener? = null

    var isTouchEnabled = true

    protected fun initAttributes(attrArray: TypedArray) {
        mCircleXRadius = attrArray.getDimension(
            R.styleable.CircularSeekBar_circle_x_radius,
            DEFAULT_CIRCLE_X_RADIUS * DPTOPX_SCALE
        )
        mCircleYRadius = attrArray.getDimension(
            R.styleable.CircularSeekBar_circle_y_radius,
            DEFAULT_CIRCLE_Y_RADIUS * DPTOPX_SCALE
        )
        mPointerRadius = attrArray.getDimension(
            R.styleable.CircularSeekBar_pointer_radius,
            DEFAULT_POINTER_RADIUS * DPTOPX_SCALE
        )
        mPointerHaloWidth = attrArray.getDimension(
            R.styleable.CircularSeekBar_pointer_halo_width,
            DEFAULT_POINTER_HALO_WIDTH * DPTOPX_SCALE
        )
        mPointerHaloBorderWidth = attrArray.getDimension(
            R.styleable.CircularSeekBar_pointer_halo_border_width,
            DEFAULT_POINTER_HALO_BORDER_WIDTH * DPTOPX_SCALE
        )
        mCircleStrokeWidth = attrArray.getDimension(
            R.styleable.CircularSeekBar_circle_stroke_width,
            DEFAULT_CIRCLE_STROKE_WIDTH * DPTOPX_SCALE
        )
        mPointerColor =
            attrArray.getColor(R.styleable.CircularSeekBar_pointer_color, DEFAULT_POINTER_COLOR)
        mPointerHaloColor = attrArray.getColor(
            R.styleable.CircularSeekBar_pointer_halo_color,
            DEFAULT_POINTER_HALO_COLOR
        )
        mPointerHaloColorOnTouch = attrArray.getColor(
            R.styleable.CircularSeekBar_pointer_halo_color_ontouch,
            DEFAULT_POINTER_HALO_COLOR_ONTOUCH
        )
        mCircleColor =
            attrArray.getColor(R.styleable.CircularSeekBar_circle_color, DEFAULT_CIRCLE_COLOR)
        mCircleProgressColor = attrArray.getColor(
            R.styleable.CircularSeekBar_circle_progress_color,
            DEFAULT_CIRCLE_PROGRESS_COLOR
        )
        mCircleFillColor =
            attrArray.getColor(R.styleable.CircularSeekBar_circle_fill, DEFAULT_CIRCLE_FILL_COLOR)
        mPointerAlpha = Color.alpha(mPointerHaloColor)
        mPointerAlphaOnTouch = attrArray.getInt(
            R.styleable.CircularSeekBar_pointer_alpha_ontouch,
            DEFAULT_POINTER_ALPHA_ONTOUCH
        )
        if (mPointerAlphaOnTouch > 255 || mPointerAlphaOnTouch < 0) {
            mPointerAlphaOnTouch = DEFAULT_POINTER_ALPHA_ONTOUCH
        }
        mMax = attrArray.getInt(R.styleable.CircularSeekBar_max, DEFAULT_MAX)
        mProgress = attrArray.getInt(R.styleable.CircularSeekBar_progress, DEFAULT_PROGRESS)
        mCustomRadii = attrArray.getBoolean(
            R.styleable.CircularSeekBar_use_custom_radii,
            DEFAULT_USE_CUSTOM_RADII
        )
        mMaintainEqualCircle = attrArray.getBoolean(
            R.styleable.CircularSeekBar_maintain_equal_circle,
            DEFAULT_MAINTAIN_EQUAL_CIRCLE
        )
        mMoveOutsideCircle = attrArray.getBoolean(
            R.styleable.CircularSeekBar_move_outside_circle,
            DEFAULT_MOVE_OUTSIDE_CIRCLE
        )
        isLockEnabled =
            attrArray.getBoolean(R.styleable.CircularSeekBar_lock_enabled, DEFAULT_LOCK_ENABLED)

        // Modulo 360 right now to avoid constant conversion
        mStartAngle = (360f + attrArray.getFloat(
            R.styleable.CircularSeekBar_start_angle,
            DEFAULT_START_ANGLE
        ) % 360f) % 360f
        mEndAngle = (360f + attrArray.getFloat(
            R.styleable.CircularSeekBar_end_angle,
            DEFAULT_END_ANGLE
        ) % 360f) % 360f
        if (mStartAngle == mEndAngle) {
            //mStartAngle = mStartAngle + 1f;
            mEndAngle = mEndAngle - .1f
        }
    }
    protected fun initPaints() {
        mCirclePaint = Paint()
        mCirclePaint!!.isAntiAlias = true
        mCirclePaint!!.isDither = true
        mCirclePaint!!.color = mCircleColor
        mCirclePaint!!.strokeWidth = mCircleStrokeWidth
        mCirclePaint!!.style = Paint.Style.STROKE
        mCirclePaint!!.strokeJoin = Paint.Join.ROUND
        mCirclePaint!!.strokeCap = Paint.Cap.ROUND
        mCircleFillPaint = Paint()
        mCircleFillPaint!!.isAntiAlias = true
        mCircleFillPaint!!.isDither = true
        mCircleFillPaint!!.color = mCircleFillColor
        mCircleFillPaint!!.style = Paint.Style.FILL
        mCircleProgressPaint = Paint()
        mCircleProgressPaint!!.isAntiAlias = true
        mCircleProgressPaint!!.isDither = true
        mCircleProgressPaint!!.color = mCircleProgressColor
        mCircleProgressPaint!!.strokeWidth = mCircleStrokeWidth
        mCircleProgressPaint!!.style = Paint.Style.STROKE
        mCircleProgressPaint!!.strokeJoin = Paint.Join.ROUND
        mCircleProgressPaint!!.strokeCap = Paint.Cap.ROUND
        mCircleProgressGlowPaint = Paint()
        mCircleProgressGlowPaint!!.set(mCircleProgressPaint)
        mCircleProgressGlowPaint!!.maskFilter =
            BlurMaskFilter(5f * DPTOPX_SCALE, BlurMaskFilter.Blur.NORMAL)
        mPointerPaint = Paint()
        mPointerPaint!!.isAntiAlias = true
        mPointerPaint!!.isDither = true
        mPointerPaint!!.style = Paint.Style.FILL
        mPointerPaint!!.color = mPointerColor
        mPointerPaint!!.strokeWidth = mPointerRadius
        mPointerHaloPaint = Paint()
        mPointerHaloPaint!!.set(mPointerPaint)
        mPointerHaloPaint!!.color = mPointerHaloColor
        mPointerHaloPaint!!.alpha = mPointerAlpha
        mPointerHaloPaint!!.strokeWidth = mPointerRadius + mPointerHaloWidth
        mPointerHaloBorderPaint = Paint()
        mPointerHaloBorderPaint!!.set(mPointerPaint)
        mPointerHaloBorderPaint!!.strokeWidth = mPointerHaloBorderWidth
        mPointerHaloBorderPaint!!.style = Paint.Style.STROKE
    }

    protected fun calculateTotalDegrees() {
        mTotalCircleDegrees =
            (360f - (mStartAngle - mEndAngle)) % 360f // Length of the entire circle/arc
        if (mTotalCircleDegrees <= 0f) {
            mTotalCircleDegrees = 360f
        }
    }
    protected fun calculateProgressDegrees() {
        mProgressDegrees = mPointerPosition - mStartAngle // Verified
        mProgressDegrees =
            if (mProgressDegrees < 0) 360f + mProgressDegrees else mProgressDegrees // Verified
    }
    protected fun calculatePointerAngle() {
        val progressPercent = mProgress.toFloat() / mMax.toFloat()
        mPointerPosition = progressPercent * mTotalCircleDegrees + mStartAngle
        mPointerPosition = mPointerPosition % 360f
    }

    protected fun calculatePointerXYPosition() {
        var pm = PathMeasure(mCircleProgressPath, false)
        var returnValue = pm.getPosTan(pm.length, mPointerPositionXY, null)
        if (!returnValue) {
            pm = PathMeasure(mCirclePath, false)
            returnValue = pm.getPosTan(0f, mPointerPositionXY, null)
        }
    }

    protected fun initPaths() {
        mCirclePath = Path()
        mCirclePath!!.addArc(mCircleRectF, mStartAngle, mTotalCircleDegrees)
        mCircleProgressPath = Path()
        mCircleProgressPath!!.addArc(mCircleRectF, mStartAngle, mProgressDegrees)
    }

    protected fun initRects() {
        mCircleRectF[-mCircleWidth, -mCircleHeight, mCircleWidth] = mCircleHeight
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate((this.width / 2).toFloat(), (this.height / 2).toFloat())
        canvas.drawPath(mCirclePath!!, mCirclePaint!!)
        canvas.drawPath(mCircleProgressPath!!, mCircleProgressGlowPaint!!)
        canvas.drawPath(mCircleProgressPath!!, mCircleProgressPaint!!)
        canvas.drawPath(mCirclePath!!, mCircleFillPaint!!)
        canvas.drawCircle(
            mPointerPositionXY[0], mPointerPositionXY[1], mPointerRadius + mPointerHaloWidth,
            mPointerHaloPaint!!
        )
        canvas.drawCircle(
            mPointerPositionXY[0], mPointerPositionXY[1], mPointerRadius,
            mPointerPaint!!
        )
        if (mUserIsMovingPointer) {
            canvas.drawCircle(
                mPointerPositionXY[0], mPointerPositionXY[1],
                mPointerRadius + mPointerHaloWidth + mPointerHaloBorderWidth / 2f,
                mPointerHaloBorderPaint!!
            )
        }
    }

    var progress: Int

        get() = Math.round(mMax.toFloat() * mProgressDegrees / mTotalCircleDegrees)
        set(progress) {
            if (mProgress != progress) {
                mProgress = progress
                if (mOnCircularSeekBarChangeListener != null) {
                    mOnCircularSeekBarChangeListener!!.onProgressChanged(this, progress, false)
                }
                recalculateAll()
                invalidate()
            }
        }

    protected fun setProgressBasedOnAngle(angle: Float) {
        mPointerPosition = angle
        calculateProgressDegrees()
        mProgress = Math.round(mMax.toFloat() * mProgressDegrees / mTotalCircleDegrees)
    }

    protected fun recalculateAll() {
        calculateTotalDegrees()
        calculatePointerAngle()
        calculateProgressDegrees()
        initRects()
        initPaths()
        calculatePointerXYPosition()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        if (mMaintainEqualCircle) {
            val min = Math.min(width, height)
            setMeasuredDimension(min, min)
        } else {
            setMeasuredDimension(width, height)
        }

        mCircleHeight =
            height.toFloat() / 2f - mCircleStrokeWidth - mPointerRadius - mPointerHaloBorderWidth * 1.5f
        mCircleWidth =
            width.toFloat() / 2f - mCircleStrokeWidth - mPointerRadius - mPointerHaloBorderWidth * 1.5f

        if (mCustomRadii) {
            if (mCircleYRadius - mCircleStrokeWidth - mPointerRadius - mPointerHaloBorderWidth < mCircleHeight) {
                mCircleHeight =
                    mCircleYRadius - mCircleStrokeWidth - mPointerRadius - mPointerHaloBorderWidth * 1.5f
            }
            if (mCircleXRadius - mCircleStrokeWidth - mPointerRadius - mPointerHaloBorderWidth < mCircleWidth) {
                mCircleWidth =
                    mCircleXRadius - mCircleStrokeWidth - mPointerRadius - mPointerHaloBorderWidth * 1.5f
            }
        }
        if (mMaintainEqualCircle) {
            val min = Math.min(mCircleHeight, mCircleWidth)
            mCircleHeight = min
            mCircleWidth = min
        }
        recalculateAll()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isTouchEnabled) {
            return false
        }

        val x = event.x - width / 2
        val y = event.y - height / 2

        val distanceX = mCircleRectF.centerX() - x
        val distanceY = mCircleRectF.centerY() - y

        val touchEventRadius =
            Math.sqrt(Math.pow(distanceX.toDouble(), 2.0) + Math.pow(distanceY.toDouble(), 2.0))
                .toFloat()
        val minimumTouchTarget =
            MIN_TOUCH_TARGET_DP * DPTOPX_SCALE // Convert minimum touch target into px
        var additionalRadius: Float // Either uses the minimumTouchTarget size or larger if the ring/pointer is larger
        additionalRadius =
            if (mCircleStrokeWidth < minimumTouchTarget) { // If the width is less than the minimumTouchTarget, use the minimumTouchTarget
                minimumTouchTarget / 2
            } else {
                mCircleStrokeWidth / 2 // Otherwise use the width
            }
        val outerRadius = Math.max(
            mCircleHeight,
            mCircleWidth
        ) + additionalRadius // Max outer radius of the circle, including the minimumTouchTarget or wheel width
        val innerRadius = Math.min(
            mCircleHeight,
            mCircleWidth
        ) - additionalRadius // Min inner radius of the circle, including the minimumTouchTarget or wheel width
        additionalRadius =
            if (mPointerRadius < minimumTouchTarget / 2) { // If the pointer radius is less than the minimumTouchTarget, use the minimumTouchTarget
                minimumTouchTarget / 2
            } else {
                mPointerRadius // Otherwise use the radius
            }
        var touchAngle: Float
        touchAngle =
            (Math.atan2(y.toDouble(), x.toDouble()) / Math.PI * 180 % 360).toFloat() // Verified
        touchAngle = if (touchAngle < 0) 360 + touchAngle else touchAngle // Verified
        cwDistanceFromStart = touchAngle - mStartAngle // Verified
        cwDistanceFromStart =
            if (cwDistanceFromStart < 0) 360f + cwDistanceFromStart else cwDistanceFromStart // Verified
        ccwDistanceFromStart = 360f - cwDistanceFromStart // Verified
        cwDistanceFromEnd = touchAngle - mEndAngle // Verified
        cwDistanceFromEnd =
            if (cwDistanceFromEnd < 0) 360f + cwDistanceFromEnd else cwDistanceFromEnd // Verified
        ccwDistanceFromEnd = 360f - cwDistanceFromEnd // Verified
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // These are only used for ACTION_DOWN for handling if the pointer was the part that was touched
                val pointerRadiusDegrees = (mPointerRadius * 180 / (Math.PI * Math.max(
                    mCircleHeight,
                    mCircleWidth
                ))).toFloat()
                cwDistanceFromPointer = touchAngle - mPointerPosition
                cwDistanceFromPointer =
                    if (cwDistanceFromPointer < 0) 360f + cwDistanceFromPointer else cwDistanceFromPointer
                ccwDistanceFromPointer = 360f - cwDistanceFromPointer
                // This is for if the first touch is on the actual pointer.
                if (touchEventRadius >= innerRadius && touchEventRadius <= outerRadius && (cwDistanceFromPointer <= pointerRadiusDegrees || ccwDistanceFromPointer <= pointerRadiusDegrees)) {
                    setProgressBasedOnAngle(mPointerPosition)
                    lastCWDistanceFromStart = cwDistanceFromStart
                    mIsMovingCW = true
                    mPointerHaloPaint!!.alpha = mPointerAlphaOnTouch
                    mPointerHaloPaint!!.color = mPointerHaloColorOnTouch
                    recalculateAll()
                    invalidate()
                    if (mOnCircularSeekBarChangeListener != null) {
                        mOnCircularSeekBarChangeListener!!.onStartTrackingTouch(this)
                    }
                    mUserIsMovingPointer = true
                    lockAtEnd = false
                    lockAtStart = false
                } else if (cwDistanceFromStart > mTotalCircleDegrees) { // If the user is touching outside of the start AND end
                    mUserIsMovingPointer = false
                    return false
                } else if (touchEventRadius >= innerRadius && touchEventRadius <= outerRadius) { // If the user is touching near the circle
                    setProgressBasedOnAngle(touchAngle)
                    lastCWDistanceFromStart = cwDistanceFromStart
                    mIsMovingCW = true
                    mPointerHaloPaint!!.alpha = mPointerAlphaOnTouch
                    mPointerHaloPaint!!.color = mPointerHaloColorOnTouch
                    recalculateAll()
                    invalidate()
                    if (mOnCircularSeekBarChangeListener != null) {
                        mOnCircularSeekBarChangeListener!!.onStartTrackingTouch(this)
                        mOnCircularSeekBarChangeListener!!.onProgressChanged(this, mProgress, true)
                    }
                    mUserIsMovingPointer = true
                    lockAtEnd = false
                    lockAtStart = false
                } else { // If the user is not touching near the circle
                    mUserIsMovingPointer = false
                    return false
                }
            }

            MotionEvent.ACTION_MOVE -> if (mUserIsMovingPointer) {
                if (lastCWDistanceFromStart < cwDistanceFromStart) {
                    if (cwDistanceFromStart - lastCWDistanceFromStart > 180f && !mIsMovingCW) {
                        lockAtStart = true
                        lockAtEnd = false
                    } else {
                        mIsMovingCW = true
                    }
                } else {
                    if (lastCWDistanceFromStart - cwDistanceFromStart > 180f && mIsMovingCW) {
                        lockAtEnd = true
                        lockAtStart = false
                    } else {
                        mIsMovingCW = false
                    }
                }
                if (lockAtStart && mIsMovingCW) {
                    lockAtStart = false
                }
                if (lockAtEnd && !mIsMovingCW) {
                    lockAtEnd = false
                }
                if (lockAtStart && !mIsMovingCW && ccwDistanceFromStart > 90) {
                    lockAtStart = false
                }
                if (lockAtEnd && mIsMovingCW && cwDistanceFromEnd > 90) {
                    lockAtEnd = false
                }
                // Fix for passing the end of a semi-circle quickly
                if (!lockAtEnd && cwDistanceFromStart > mTotalCircleDegrees && mIsMovingCW && lastCWDistanceFromStart < mTotalCircleDegrees) {
                    lockAtEnd = true
                }
                if (lockAtStart && isLockEnabled) {
                    // TODO: Add a check if mProgress is already 0, in which case don't call the listener
                    mProgress = 0
                    recalculateAll()
                    invalidate()
                    if (mOnCircularSeekBarChangeListener != null) {
                        mOnCircularSeekBarChangeListener!!.onProgressChanged(this, mProgress, true)
                    }
                } else if (lockAtEnd && isLockEnabled) {
                    mProgress = mMax
                    recalculateAll()
                    invalidate()
                    if (mOnCircularSeekBarChangeListener != null) {
                        mOnCircularSeekBarChangeListener!!.onProgressChanged(this, mProgress, true)
                    }
                } else if (mMoveOutsideCircle || touchEventRadius <= outerRadius) {
                    if (cwDistanceFromStart <= mTotalCircleDegrees) {
                        setProgressBasedOnAngle(touchAngle)
                    }
                    recalculateAll()
                    invalidate()
                    if (mOnCircularSeekBarChangeListener != null) {
                        mOnCircularSeekBarChangeListener!!.onProgressChanged(this, mProgress, true)
                    }
                } else {
                    //break
                }
                lastCWDistanceFromStart = cwDistanceFromStart
            } else {
                return false
            }

            MotionEvent.ACTION_UP -> {
                mPointerHaloPaint!!.alpha = mPointerAlpha
                mPointerHaloPaint!!.color = mPointerHaloColor
                if (mUserIsMovingPointer) {
                    mUserIsMovingPointer = false
                    invalidate()
                    if (mOnCircularSeekBarChangeListener != null) {
                        mOnCircularSeekBarChangeListener!!.onStopTrackingTouch(this)
                    }
                } else {
                    return false
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                mPointerHaloPaint!!.alpha = mPointerAlpha
                mPointerHaloPaint!!.color = mPointerHaloColor
                mUserIsMovingPointer = false
                invalidate()
            }
        }
        if (event.action == MotionEvent.ACTION_MOVE && parent != null) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return true
    }

    protected fun init(attrs: AttributeSet?, defStyle: Int) {
        val attrArray =
            context.obtainStyledAttributes(attrs, R.styleable.CircularSeekBar, defStyle, 0)
        initAttributes(attrArray)
        attrArray.recycle()
        initPaints()
    }

    constructor(context: Context?) : super(context) {
        init(null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val state = Bundle()
        state.putParcelable("PARENT", superState)
        state.putInt("MAX", mMax)
        state.putInt("PROGRESS", mProgress)
        state.putInt("mCircleColor", mCircleColor)
        state.putInt("mCircleProgressColor", mCircleProgressColor)
        state.putInt("mPointerColor", mPointerColor)
        state.putInt("mPointerHaloColor", mPointerHaloColor)
        state.putInt("mPointerHaloColorOnTouch", mPointerHaloColorOnTouch)
        state.putInt("mPointerAlpha", mPointerAlpha)
        state.putInt("mPointerAlphaOnTouch", mPointerAlphaOnTouch)
        state.putBoolean("lockEnabled", isLockEnabled)
        state.putBoolean("isTouchEnabled", isTouchEnabled)
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as Bundle
        val superState = savedState.getParcelable<Parcelable>("PARENT")
        super.onRestoreInstanceState(superState)
        mMax = savedState.getInt("MAX")
        mProgress = savedState.getInt("PROGRESS")
        mCircleColor = savedState.getInt("mCircleColor")
        mCircleProgressColor = savedState.getInt("mCircleProgressColor")
        mPointerColor = savedState.getInt("mPointerColor")
        mPointerHaloColor = savedState.getInt("mPointerHaloColor")
        mPointerHaloColorOnTouch = savedState.getInt("mPointerHaloColorOnTouch")
        mPointerAlpha = savedState.getInt("mPointerAlpha")
        mPointerAlphaOnTouch = savedState.getInt("mPointerAlphaOnTouch")
        isLockEnabled = savedState.getBoolean("lockEnabled")
        isTouchEnabled = savedState.getBoolean("isTouchEnabled")
        initPaints()
        recalculateAll()
    }

    fun setOnSeekBarChangeListener(l: OnCircularSeekBarChangeListener?) {
        mOnCircularSeekBarChangeListener = l
    }

    interface OnCircularSeekBarChangeListener {
        fun onProgressChanged(circularSeekBar: CircularSeekBar?, progress: Int, fromUser: Boolean)
        fun onStopTrackingTouch(seekBar: CircularSeekBar?)
        fun onStartTrackingTouch(seekBar: CircularSeekBar?)
    }

    var circleColor: Int

        get() = mCircleColor

        set(color) {
            mCircleColor = color
            mCirclePaint!!.color = mCircleColor
            invalidate()
        }
    var circleProgressColor: Int

        get() = mCircleProgressColor

        set(color) {
            mCircleProgressColor = color
            mCircleProgressPaint!!.color = mCircleProgressColor
            invalidate()
        }
    var pointerColor: Int

        get() = mPointerColor

        set(color) {
            mPointerColor = color
            mPointerPaint!!.color = mPointerColor
            invalidate()
        }
    var pointerHaloColor: Int

        get() = mPointerHaloColor

        set(color) {
            mPointerHaloColor = color
            mPointerHaloPaint!!.color = mPointerHaloColor
            invalidate()
        }
    var pointerAlpha: Int

        get() = mPointerAlpha

        set(alpha) {
            if (alpha >= 0 && alpha <= 255) {
                mPointerAlpha = alpha
                mPointerHaloPaint!!.alpha = mPointerAlpha
                invalidate()
            }
        }
    var pointerAlphaOnTouch: Int

        get() = mPointerAlphaOnTouch

        set(alpha) {
            if (alpha >= 0 && alpha <= 255) {
                mPointerAlphaOnTouch = alpha
            }
        }
    var circleFillColor: Int

        get() = mCircleFillColor

        set(color) {
            mCircleFillColor = color
            mCircleFillPaint!!.color = mCircleFillColor
            invalidate()
        }

    @get:Synchronized
    var max: Int

        get() = mMax
        set(max) {
            if (max > 0) { // Check to make sure it's greater than zero
                if (max <= mProgress) {
                    mProgress =
                        0 // If the new max is less than current progress, set progress to zero
                    if (mOnCircularSeekBarChangeListener != null) {
                        mOnCircularSeekBarChangeListener!!.onProgressChanged(this, mProgress, false)
                    }
                }
                mMax = max
                recalculateAll()
                invalidate()
            }
        }

    companion object {
        // Default values
        protected const val DEFAULT_CIRCLE_X_RADIUS = 30f
        protected const val DEFAULT_CIRCLE_Y_RADIUS = 30f
        protected const val DEFAULT_POINTER_RADIUS = 7f
        protected const val DEFAULT_POINTER_HALO_WIDTH = 6f
        protected const val DEFAULT_POINTER_HALO_BORDER_WIDTH = 2f
        protected const val DEFAULT_CIRCLE_STROKE_WIDTH = 5f
        protected const val DEFAULT_START_ANGLE =
            270f // Geometric (clockwise, relative to 3 o'clock)
        protected const val DEFAULT_END_ANGLE = 270f // Geometric (clockwise, relative to 3 o'clock)
        protected const val DEFAULT_MAX = 100
        protected const val DEFAULT_PROGRESS = 0
        protected const val DEFAULT_CIRCLE_COLOR = Color.DKGRAY
        protected val DEFAULT_CIRCLE_PROGRESS_COLOR = Color.argb(235, 74, 138, 255)
        protected val DEFAULT_POINTER_COLOR = Color.argb(235, 74, 138, 255)
        protected val DEFAULT_POINTER_HALO_COLOR = Color.argb(135, 74, 138, 255)
        protected val DEFAULT_POINTER_HALO_COLOR_ONTOUCH = Color.argb(135, 74, 138, 255)
        protected const val DEFAULT_CIRCLE_FILL_COLOR = Color.TRANSPARENT
        protected const val DEFAULT_POINTER_ALPHA = 135
        protected const val DEFAULT_POINTER_ALPHA_ONTOUCH = 100
        protected const val DEFAULT_USE_CUSTOM_RADII = false
        protected const val DEFAULT_MAINTAIN_EQUAL_CIRCLE = true
        protected const val DEFAULT_MOVE_OUTSIDE_CIRCLE = false
        protected const val DEFAULT_LOCK_ENABLED = true
    }
}