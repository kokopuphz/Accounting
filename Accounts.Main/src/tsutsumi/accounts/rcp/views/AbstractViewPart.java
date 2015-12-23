package tsutsumi.accounts.rcp.views;

import org.eclipse.swt.SWT;
import org.eclipse.ui.ISizeProvider;
import org.eclipse.ui.part.ViewPart;

public abstract class AbstractViewPart extends ViewPart implements ISizeProvider {
	protected int minHeight = ISizeProvider.INFINITE;
	protected int maxHeight = ISizeProvider.INFINITE;
	protected int minWidth = ISizeProvider.INFINITE;
	protected int maxWidth = ISizeProvider.INFINITE;
    private int quantizedWidth = ISizeProvider.INFINITE;
    private int quantizedHeight = ISizeProvider.INFINITE;
    private int fixedArea = ISizeProvider.INFINITE;
	public void setFocus() {
	}

    public int getSizeFlags(boolean width) {
        int flags = 0;
        if (width) {
            if (minWidth != ISizeProvider.INFINITE) {
                flags |= SWT.MIN;
            }
            if (maxWidth != ISizeProvider.INFINITE) {
                flags |= SWT.MAX;
            }
            if (quantizedWidth != ISizeProvider.INFINITE || fixedArea != ISizeProvider.INFINITE) {
                flags |= SWT.FILL;
            }
            if (fixedArea != ISizeProvider.INFINITE) {
                flags |= SWT.WRAP;
            }
        } else {
            if (minHeight != ISizeProvider.INFINITE) {
                flags |= SWT.MIN;
            }
            if (maxHeight != ISizeProvider.INFINITE) {
                flags |= SWT.MAX;
            }
            if (quantizedHeight != ISizeProvider.INFINITE || fixedArea != ISizeProvider.INFINITE) {
                flags |= SWT.FILL;
            }
            if (fixedArea != ISizeProvider.INFINITE) {
                flags |= SWT.WRAP;
            }			
        }

        return flags;
    }

    public int computePreferredSize(boolean width, int availableParallel,
            int availablePerpendicular, int preferredResult) {

        int result = preferredResult;

        if (fixedArea != ISizeProvider.INFINITE) {
            // Try to maintain a fixed area
            result = (availablePerpendicular != 0) ? fixedArea / availablePerpendicular : 0;
            if (result < 30) result = 30;
        }

        if (width) {
            if (quantizedWidth != ISizeProvider.INFINITE && quantizedWidth != 0) {
                // Jump to the nearest multiple of the quantized size
                result = Math.min(result + quantizedWidth/2, availableParallel);
                result = result - (result % quantizedWidth);
            }
            if (minWidth != ISizeProvider.INFINITE) {
                // Ensure we go no smaller than the minimum size
                if (result < minWidth) result = minWidth;
            }
            if (maxWidth != ISizeProvider.INFINITE) {
                // Ensure we go no larger than the maximum size
                if (result > maxWidth) result = maxWidth;
            }
        } else {
            // Jump to the nearest multiple of the quantized size
            if (quantizedHeight != ISizeProvider.INFINITE && quantizedHeight != 0) {
                result = Math.min(result + quantizedHeight/2, availableParallel);
                result = result - (result % quantizedHeight);
            }
            if (minHeight != ISizeProvider.INFINITE) {
                // Ensure we go no smaller than the minimum size
                if (result < minHeight) result = minHeight;
            }
            if (maxHeight != ISizeProvider.INFINITE) {
                // Ensure we go no larger than the maximum size
                if (result > maxHeight) result = maxHeight;
            }
        }

        // Ensure that we do not use more than the available space
        if (result > availableParallel) result = availableParallel;
        if (result < 0) result = 0;
        return result;
    }

}
