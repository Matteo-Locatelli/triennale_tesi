package p3d4amb.sat.lib.session;

public abstract class DepthCertBase {

	/**
	 * The Enum Solution.
	 */
	public enum Solution {
	
		/** The right. */
		RIGHT,
		/** The wrong. */
		WRONG,
		/** The null. */
		NULL
	}

	/** The certifier status. */
	protected CertifierStatus certifierStatus;

	/**
	 * The Class CertifierStatus.
	 */
	static public class CertifierStatus {
	
		/** The current depth. */
		// current Depth
		public int currentDepth;
	
		/** The current result. */
		// current status
		public TestSession.Result currentResult;
	
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			switch (currentResult) {
			case FINISH_CERTIFIED:
				return "CERTIFICATO a livello: " + currentDepth;
			case FINISH_NOT_CERTIFIED:
				return "FINITO ma NON CERTIFICATO fino a livello: " + currentDepth;
			case CONTINUE:
				return "INCONCLUSIVO (testando " + currentDepth + ")";
			}
			return "";
		}
	}

	/**
	 * Gets the current depth.
	 *
	 * @return the current depth
	 */
	public int getCurrentDepth() {
		return certifierStatus.currentDepth;
	}

	public abstract void computeNextDepth(Solution sol);

	/**
	 * 
	 * @return
	 */
	abstract public CertifierStatus getCurrentStatus();
	
}
